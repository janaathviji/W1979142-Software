// Janaath Vijithavarnan
// W1979142

// This is the camera flow screen
// This file manages the camera workflow for skin disease detection.
// It handles two steps which is capturing an image with the camera and then previewing it with ML analysis


package com.example.dermoinspect.ui.camera

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable


@Composable
fun CameraFlowScreen(
    onAnalyzeImage: (Uri, String, Float, List<Pair<String, Float>>) -> Unit,
    onBack: () -> Unit
) {
    var capturedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    if (capturedImageUri == null) {
        // Camera screen
        CameraScreen(
            onImageCaptured = { uri ->
                capturedImageUri = uri
            },
            onBack = onBack
        )
    } else {

        // The screen switches between CameraScreen and ImagePreviewScreen based on whether a photo has been taken
        // Users can retake the photo or confirm to proceed with the analysis
        // Image preview + ML
        ImagePreviewScreen(
            imageUri = capturedImageUri!!,
            onConfirm = { diseaseName, confidence, topPredictions ->
                onAnalyzeImage(
                    capturedImageUri!!,
                    diseaseName,
                    confidence,
                    topPredictions
                )
            },
            onRetake = {
                capturedImageUri = null
            }
        )
    }
}
