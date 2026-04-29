//Added to save image to history

package com.example.dermoinspect.data.model

import com.google.firebase.Timestamp
import java.util.Date


// This file defines models for storing and managing scan history data
// ScanHistory represents a saved skin scan record containing the scan ID, user ID,
// image URL from Firebase Storage, detected disease type, confidence score, top prediction results, timestamp, and malignancy status
data class ScanHistory(
    val id: String = "",
    val userId: String = "",
    val imageUrl: String = "",           // This is the firebase Storage URL
    val diseaseType: String = "",
    val confidence: Float = 0f,
    val topPredictions: List<Prediction> = emptyList(),
    val timestamp: Date = Date(),
    val isMalignant: Boolean = false
)

// This is the prediction holds individual disease predictions with the disease name and confidence score
data class Prediction(
    val diseaseName: String = "",
    val confidence: Float = 0f
)

// This is for Firebase
// toMap() converts a ScanHistory object into a Map format that can be easily saved to Firebase database.

fun ScanHistory.toMap(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "userId" to userId,
        "imageUrl" to imageUrl,
        "diseaseType" to diseaseType,
        "confidence" to confidence,
        "topPredictions" to topPredictions.map {
            mapOf(
                "diseaseName" to it.diseaseName,
                "confidence" to it.confidence
            )
        },
        "timestamp" to Timestamp(timestamp),
        "isMalignant" to isMalignant
    )
}
