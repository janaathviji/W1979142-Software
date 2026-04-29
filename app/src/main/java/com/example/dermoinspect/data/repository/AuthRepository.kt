// Janaath Vijithavarnan
// W1979142

package com.example.dermoinspect.data.repository

import com.example.dermoinspect.data.model.User
import com.example.dermoinspect.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await



// This is the repository for the authentication process
class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    // This is to get the current user
    fun getCurrentUser(): FirebaseUser? = auth.currentUser


    // This is to check if the user is logged in
    fun isUserLoggedIn(): Boolean = auth.currentUser != null


    // This is the sign up process
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<User> {
        return try {
            // This is to create the user with Firebase Authentication
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("User creation failed")

            // This is to create the user firebase
            val user = User(
                uid = firebaseUser.uid,
                email = email,
                firstName = firstName,
                lastName = lastName,
                createdAt = System.currentTimeMillis()
            )

            firestore.collection(Constants.COLLECTION_USERS)
                .document(firebaseUser.uid)
                .set(user)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //This is the login process
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Login failed")

            // This is to get the user data from the firestore
            val userDoc = firestore.collection(Constants.COLLECTION_USERS)
                .document(firebaseUser.uid)
                .get()
                .await()

            val user = userDoc.toObject(User::class.java)
                ?: throw Exception("User data not found")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //This is the logout process
    fun logout() {
        auth.signOut()
    }


    // This is where the users data will get taken from the firestore
    suspend fun getUserData(uid: String): Result<User> {
        return try {
            val userDoc = firestore.collection(Constants.COLLECTION_USERS)
                .document(uid)
                .get()
                .await()

            val user = userDoc.toObject(User::class.java)
                ?: throw Exception("User not found")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // This is to update the user profile
    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            firestore.collection(Constants.COLLECTION_USERS)
                .document(user.uid)
                .set(user)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}