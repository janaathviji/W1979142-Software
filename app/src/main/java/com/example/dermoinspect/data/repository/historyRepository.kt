// Janaath Vijithavarnan
// W1979142

// This is added to save image to history

package com.example.dermoinspect.data.repository

import android.content.Context
import android.net.Uri
import com.example.dermoinspect.data.model.Prediction
import com.example.dermoinspect.data.model.ScanHistory
import com.example.dermoinspect.data.model.toMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class HistoryRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val COLLECTION_SCANS = "scan_history"
        private const val STORAGE_PATH = "scan_images"
    }

    // This is to save the scanned results to firebase
    suspend fun saveScanResult(
        context: Context,
        imageUri: Uri,
        diseaseType: String,
        confidence: Float,
        topPredictions: List<Pair<String, Float>>,
        isMalignant: Boolean
    ): Result<String> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            // Upload image to Firebase Storage
            val imageUrl = uploadImage(context, imageUri, userId)

            // This creates a scan history object
            val scanId = UUID.randomUUID().toString()
            val scanHistory = ScanHistory(
                id = scanId,
                userId = userId,
                imageUrl = imageUrl,
                diseaseType = diseaseType,
                confidence = confidence,
                topPredictions = topPredictions.map {
                    Prediction(it.first, it.second)
                },
                timestamp = Date(),
                isMalignant = isMalignant
            )

            // This is where it saves to firestore
            firestore.collection(COLLECTION_SCANS)
                .document(scanId)
                .set(scanHistory.toMap())
                .await()

            Result.success(scanId)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    //This upload the image to firebase
    private suspend fun uploadImage(
        context: Context,
        imageUri: Uri,
        userId: String
    ): String {
        val fileName = "${UUID.randomUUID()}.jpg"
        val storageRef = storage.reference
            .child(STORAGE_PATH)
            .child(userId)
            .child(fileName)

        // this is the upload file
        storageRef.putFile(imageUri).await()

        // This is the download URL of the image
        return storageRef.downloadUrl.await().toString()
    }


    //This is to get all the scan history for the current users
    suspend fun getScanHistory(): Result<List<ScanHistory>> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            val snapshot = firestore.collection(COLLECTION_SCANS)
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val history = snapshot.documents.mapNotNull { doc ->
                try {
                    ScanHistory(
                        id = doc.id,
                        userId = doc.getString("userId") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: "",
                        diseaseType = doc.getString("diseaseType") ?: "",
                        confidence = doc.getDouble("confidence")?.toFloat() ?: 0f,
                        topPredictions = (doc.get("topPredictions") as? List<*>)?.mapNotNull { pred ->
                            val predMap = pred as? Map<*, *>
                            predMap?.let {
                                Prediction(
                                    diseaseName = it["diseaseName"] as? String ?: "",
                                    confidence = (it["confidence"] as? Double)?.toFloat() ?: 0f
                                )
                            }
                        } ?: emptyList(),
                        timestamp = doc.getTimestamp("timestamp")?.toDate() ?: Date(),
                        isMalignant = doc.getBoolean("isMalignant") ?: false
                    )
                } catch (e: Exception) {
                    null
                }
            }

            Result.success(history)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // This is to delete a scan from the history
    suspend fun deleteScan(scanId: String): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            // This is to get the scan to verify and get image URL
            val doc = firestore.collection(COLLECTION_SCANS)
                .document(scanId)
                .get()
                .await()

            val scanUserId = doc.getString("userId")
            if (scanUserId != userId) {
                return Result.failure(Exception("Unauthorized"))
            }

            // This is to delete the image from storage
            val imageUrl = doc.getString("imageUrl")
            imageUrl?.let {
                try {
                    val imageRef = storage.getReferenceFromUrl(it)
                    imageRef.delete().await()
                } catch (e: Exception) {
                    // This is where the image deletion failed has failed and continue with deletion
                }
            }

            // This is to delete the firestore document
            firestore.collection(COLLECTION_SCANS)
                .document(scanId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}