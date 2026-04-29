// Janaath Vijithavarnan
// W1979142

//This is the Results screen

package com.example.dermoinspect.ui.results

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dermoinspect.ui.history.HistoryViewModel
import com.example.dermoinspect.ui.history.SaveState

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dermoinspect.ui.theme.*

// This is the data class for disease information
data class DiseaseInfo(
    val name: String,
    val description: String,
    val characteristics: List<String>,
    val isMalignant: Boolean
)

// This is the disease information database
fun getDiseaseInfo(diseaseType: String): DiseaseInfo {
    return when (diseaseType) {
        "Melanoma" -> DiseaseInfo(
            name = "Melanoma",
            description = "A serious form of skin cancer that develops in melanocytes (pigment-producing cells). Early detection is crucial for successful treatment.",
            characteristics = listOf(
                "Asymmetrical shape",
                "Irregular borders",
                "Multiple colors",
                "Diameter larger than 6mm",
                "Evolving over time"
            ),
            isMalignant = true
        )
        "Melanocytic nevi" -> DiseaseInfo(
            name = "Melanocytic Nevi (Mole)",
            description = "Common benign skin growths made of melanocytes. Most are harmless, but should be monitored for changes.",
            characteristics = listOf(
                "Usually uniform in color",
                "Round or oval shape",
                "Distinct borders",
                "Generally smaller than 6mm",
                "Stable over time"
            ),
            isMalignant = false
        )
        "Basal cell carcinoma" -> DiseaseInfo(
            name = "Basal Cell Carcinoma",
            description = "The most common type of skin cancer. It grows slowly and rarely spreads, but should be treated promptly.",
            characteristics = listOf(
                "Pearly or waxy bump",
                "Flat, flesh-colored lesion",
                "May bleed easily",
                "Often appears on sun-exposed areas",
                "Slow-growing"
            ),
            isMalignant = true
        )
        "Actinic keratoses" -> DiseaseInfo(
            name = "Actinic Keratoses",
            description = "Rough, scaly patches caused by sun damage. Considered precancerous and should be monitored by a dermatologist.",
            characteristics = listOf(
                "Rough, dry, or scaly patches",
                "Flat or slightly raised",
                "Color varies (pink, red, brown)",
                "More common in sun-exposed areas",
                "May feel tender or itchy"
            ),
            isMalignant = false
        )
        "Benign keratosis-like lesions" -> DiseaseInfo(
            name = "Benign Keratosis",
            description = "Non-cancerous skin growths that appear with age. Also known as seborrheic keratoses, they are harmless but may be cosmetically concerning.",
            characteristics = listOf(
                "Waxy, stuck-on appearance",
                "Brown, black, or tan color",
                "Slightly raised",
                "Rough or smooth surface",
                "Common in older adults"
            ),
            isMalignant = false
        )
        "Dermatofibroma" -> DiseaseInfo(
            name = "Dermatofibroma",
            description = "A common benign skin nodule. Usually harmless and doesn't require treatment unless bothersome.",
            characteristics = listOf(
                "Firm, hard bump",
                "Brown or reddish color",
                "Dimples when pinched",
                "Usually on legs or arms",
                "Grows slowly"
            ),
            isMalignant = false
        )
        "Vascular lesions" -> DiseaseInfo(
            name = "Vascular Lesions",
            description = "Abnormalities of blood vessels in the skin. Most are benign and may include hemangiomas or cherry angiomas.",
            characteristics = listOf(
                "Red or purple color",
                "May blanch with pressure",
                "Various sizes and shapes",
                "Usually benign",
                "May appear at birth or develop over time"
            ),
            isMalignant = false
        )
        else -> DiseaseInfo(
            name = "Unknown",
            description = "Information not available for this condition.",
            characteristics = emptyList(),
            isMalignant = false
        )
    }
}

// This is the confidence assessment
data class ConfidenceAssessment(
    val level: String,
    val color: Color,
    val message: String,
    val showImageTips: Boolean
)

fun getConfidenceAssessment(confidence: Float): ConfidenceAssessment {
    return when {
        confidence >= 0.90f -> ConfidenceAssessment(
            level = "Very High",
            color = Color(0xFF4CAF50), // Green
            message = "Professional quality image - High reliability",
            showImageTips = false
        )
        confidence >= 0.80f -> ConfidenceAssessment(
            level = "High",
            color = Color(0xFF8BC34A), // Light Green
            message = "Good quality image - Reliable result",
            showImageTips = false
        )
        confidence >= 0.70f -> ConfidenceAssessment(
            level = "Moderate",
            color = Color(0xFFFFC107), // Yellow
            message = "Consider retaking photo in better lighting",
            showImageTips = true
        )
        confidence >= 0.60f -> ConfidenceAssessment(
            level = "Low",
            color = Color(0xFFFF9800), // Orange
            message = "Image quality may affect accuracy - Retake recommended",
            showImageTips = true
        )
        else -> ConfidenceAssessment(
            level = "Very Low",
            color = Color(0xFFF44336), // Red
            message = "Please retake photo with better quality",
            showImageTips = true
        )
    }
}


// This is the rejection screen and its shown when confidence is 0f
// This means the image failed quality checks before the model even run
// (too dark, too bright, or no texture detected)

@Composable
fun RejectionScreen(
    rejectionMessage: String, // The reason why the image was rejected (from the classifier)
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan)
            .verticalScroll(rememberScrollState())
    ) {

        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PrimaryCyan,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryNavy
                    )
                }
                Text(
                    text = "Scan Results",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // Large warning icon to make it very clear something went wrong
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Image rejected",
                tint = Color(0xFFF44336),
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // This is the main rejection heading
            Text(
                text = "Image Not Valid",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Show the specific reason returned by the classifier
            // Rejection message is show and shows what disease class it is
            // Which is Non-Skin
            Text(
                text = rejectionMessage,
                fontSize = 15.sp,
                color = PrimaryNavy.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // This is the tips card where if the image quality isn't the best, it gives
            // solution to how to take the picture properly
            // This is shown in the rejection page
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0) // Light orange
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFFF6F00)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📸 Tips for Better Results",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val tips = listOf(
                        "Use good, natural lighting",
                        "Keep camera steady and in focus",
                        "Center the lesion in frame",
                        "Avoid shadows and glare",
                        "Get close but maintain focus",
                        "Clean the camera lens"
                    )

                    tips.forEach { tip ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "•",
                                fontSize = 14.sp,
                                color = Color(0xFFE65100),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = tip,
                                fontSize = 14.sp,
                                color = Color(0xFF4E342E)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // This is the try again button
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue)
            ) {
                Text(
                    text = "Try Again",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ResultsScreen(
    imageUri: Uri?,
    diseaseType: String = "Analyzing...",
    confidence: Float = 0f,
    topPredictions: List<Pair<String, Float>>? = null,
    onBack: () -> Unit,
    historyViewModel: HistoryViewModel = viewModel(),
    isFromHistory: Boolean = false

) {
    val context = LocalContext.current
    val saveState by historyViewModel.saveState.collectAsState()


    // This is the Rejection Check
    // if confidence is 0f the image will be rejected
    // by the quality checks in SkinDiseaseClassifier before the
    // model even run.
    // Show the rejection screen instead of results.
    // diseaseType contains the rejection reason in this case.


    if (confidence == 0f) {
        RejectionScreen(
            rejectionMessage = diseaseType,
            onBack = onBack
        )
        return // It stops here and does not render the normal results screen below
    }




    if (diseaseType == "Non-skin image") {
        RejectionScreen(
            rejectionMessage = diseaseType,
            onBack = onBack
        )
        return
    }

    // Handle save state
    LaunchedEffect(saveState) {
        when (saveState) {
            is SaveState.Success -> {
                // Show success message

            }
            is SaveState.Error -> {
                // Show error message
            }
            else -> {}
        }
    }

    val diseaseInfo = getDiseaseInfo(diseaseType)
    val confidenceAssessment = getConfidenceAssessment(confidence)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan)
            .verticalScroll(rememberScrollState())
    ) {

        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PrimaryCyan,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryNavy
                    )
                }
                Text(
                    text = "Scan Results",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // This is the image Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Scanned Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // This is the primary results card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardLight)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // This is the disease type with Malignant Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Detected Condition:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = PrimaryNavy.copy(alpha = 0.7f)
                            )
                            Text(
                                text = diseaseType,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = SecondaryBlue,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // Malignant/Benign Badge
                        if (diseaseType != "Analyzing...") {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (diseaseInfo.isMalignant)
                                    Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                            ) {
                                Text(
                                    text = if (diseaseInfo.isMalignant) "Malignant" else "Benign",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (diseaseInfo.isMalignant)
                                        Color(0xFFD32F2F) else Color(0xFF388E3C),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // This is the confidence Section
                    if (confidence > 0) {
                        Text(
                            text = "Confidence Level:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = PrimaryNavy.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // This is the confidence Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = confidenceAssessment.color.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = confidenceAssessment.level,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = confidenceAssessment.color,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "${(confidence * 100).toInt()}%",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = confidenceAssessment.color
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress Bar
                        LinearProgressIndicator(
                            progress = confidence,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp),
                            color = confidenceAssessment.color,
                            trackColor = PrimaryNavy.copy(alpha = 0.1f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Confidence Message
                        Text(
                            text = confidenceAssessment.message,
                            fontSize = 13.sp,
                            color = PrimaryNavy.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Image Quality Tips (if needed)
            if (confidenceAssessment.showImageTips) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0) // Light orange
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFFFF6F00)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "📸 Tips for Better Results",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val tips = listOf(
                            "Use good, natural lighting",
                            "Keep camera steady and in focus",
                            "Center the lesion in frame",
                            "Avoid shadows and glare",
                            "Get close but maintain focus",
                            "Clean the camera lens"
                        )

                        tips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "•",
                                    fontSize = 14.sp,
                                    color = Color(0xFFE65100),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = tip,
                                    fontSize = 14.sp,
                                    color = Color(0xFF4E342E)
                                )
                            }
                        }
                    }
                }
            }

            // Top 3 Predictions are shown here if available
            if (topPredictions != null && topPredictions.size > 1) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardLight)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Alternative Possibilities",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        topPredictions.take(3).forEachIndexed { index, (disease, conf) ->
                            if (index > 0) { // Skip first because it's already shown above
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${index + 1}. $disease",
                                        fontSize = 14.sp,
                                        color = PrimaryNavy,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = "${(conf * 100).toInt()}%",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy.copy(alpha = 0.6f)
                                    )
                                }

                                if (index < topPredictions.size - 1) {
                                    Divider(
                                        color = PrimaryNavy.copy(alpha = 0.1f),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // This is the disease information card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardLight)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "About ${diseaseInfo.name}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = diseaseInfo.description,
                        fontSize = 14.sp,
                        color = PrimaryNavy.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )

                    if (diseaseInfo.characteristics.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Common Characteristics:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryNavy,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        diseaseInfo.characteristics.forEach { characteristic ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "•",
                                    fontSize = 14.sp,
                                    color = SecondaryBlue,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = characteristic,
                                    fontSize = 14.sp,
                                    color = PrimaryNavy.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // This is the action buttons such as save
            Button(
                onClick = {
                    if (imageUri != null && topPredictions != null) {
                        historyViewModel.saveScan(
                            context = context,
                            imageUri = imageUri,
                            diseaseType = diseaseType,
                            confidence = confidence,
                            topPredictions = topPredictions,
                            isMalignant = diseaseInfo.isMalignant
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue),
                enabled = !isFromHistory && saveState != SaveState.Saving
            ) {
                if (saveState == SaveState.Saving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Saving...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = when {
                            isFromHistory -> "Saved ✓"
                            saveState == SaveState.Success -> "Saved ✓"
                            else -> "Save Result"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // This is the medical disclaimer
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE) // Light red
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "⚠️ Medical Disclaimer",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC62828),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "This app is for educational purposes only and is NOT a substitute for professional medical advice, diagnosis, or treatment. Always consult a qualified dermatologist or healthcare provider for proper evaluation of any skin condition. If you notice changes in a mole or lesion, seek medical attention immediately.",
                        fontSize = 13.sp,
                        color = PrimaryNavy.copy(alpha = 0.8f),
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


