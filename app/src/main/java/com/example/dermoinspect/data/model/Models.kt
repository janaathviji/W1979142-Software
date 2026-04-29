// Janaath Vijithavarnan
// W1979142

//This file contains the main data models for the app
package com.example.dermoinspect.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// User stores account information like email, name, and profile picture
@Parcelize
data class User(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

// ScanResult saves each skin scan with the image, disease detected, and confidence score
@Parcelize
data class ScanResult(
    val id: String = "",
    val userId: String = "",
    val imageUrl: String = "",
    val localImagePath: String = "",
    val diseaseType: String = "",
    val confidence: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val diseaseInfo: DiseaseInfo? = null
) : Parcelable


// DiseaseInfo holds details about a disease including symptoms, causes, and treatments
@Parcelize
data class DiseaseInfo(
    val name: String = "",
    val description: String = "",
    val symptoms: List<String> = emptyList(),
    val causes: List<String> = emptyList(),
    val treatments: List<String> = emptyList(),
    val prevention: List<String> = emptyList(),
    val imageUrl: String? = null
) : Parcelable





// This is the ViewState that manages UI loading states like idle,
// loading, success, or error throughout the app
sealed class ViewState<out T> {
    object Idle : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val message: String, val exception: Throwable? = null) : ViewState<Nothing>()
}