// Janaath Vijithavarnan
// W1979142

// This is the image preview screen
// This file shows the captured image preview and runs the model
// It displays the photo with two buttons: retake to capture again or analyze to detect diseases.
// When analyze is clicked, it converts the image to bitmap, runs it through the classifier, and returns the disease predictions
// A loading indicator shows during analysis and error messages appear if issues occur

package com.example.dermoinspect.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dermoinspect.ml.SkinDiseaseClassifier
import com.example.dermoinspect.ui.theme.PrimaryCyan
import com.example.dermoinspect.ui.theme.PrimaryNavy
import com.example.dermoinspect.ui.theme.AccentGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImagePreviewScreen(
    imageUri: Uri,
    onConfirm: (String, Float, List<Pair<String, Float>>) -> Unit,
    onRetake: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isAnalyzing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // This is the Initialize classifier
    val classifier = remember { SkinDiseaseClassifier(context) }

    // This is the cleanup when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            classifier.close()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Preview Image",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                modifier = Modifier.padding(16.dp)
            )

            // Image Preview
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Preview Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // This is the Error message
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // This is the Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // This is the Retake Button
                Button(
                    onClick = onRetake,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    enabled = !isAnalyzing
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Retake",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                                color = Color.White
                    )
                }

                // This is the Analyze Button
                Button(
                    onClick = {
                        scope.launch {
                            isAnalyzing = true
                            errorMessage = null

                            try {
                                // Convert URI to Bitmap
                                val bitmap = uriToBitmap(context, imageUri)

                                if (bitmap != null) {
                                    // Run classification on background thread
                                    val results = withContext(Dispatchers.Default) {
                                        classifier.classifyImage(bitmap)
                                    }

                                    if (results.isNotEmpty()) {
                                        // Get top result AND all top 3
                                        val topResult = results.first()
                                        val topPredictions = results.map {
                                            it.diseaseName to it.confidence
                                        }

                                        // Pass all data to next screen
                                        onConfirm(
                                            topResult.diseaseName,
                                            topResult.confidence,
                                            topPredictions
                                        )
                                    } else {
                                        errorMessage = "Could not analyze image. Please try again."
                                    }
                                } else {
                                    errorMessage = "Failed to load image. Please try again."
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.message}"
                            } finally {
                                isAnalyzing = false
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                    enabled = !isAnalyzing
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAnalyzing) "Analyzing..." else "Analyze",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                                color = Color.White

                    )
                }
            }
        }
    }
}

// This converts URI to Bitmap
private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

