// Janaath Vijithavarnan
// W1979142

// This is the disease detail screen
package com.example.dermoinspect.ui.information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermoinspect.data.model.DiseaseCategory
import com.example.dermoinspect.data.model.DiseaseInformation
import com.example.dermoinspect.ui.theme.*


@Composable
fun DiseaseDetailScreen(
    disease: DiseaseInformation,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Symptoms", "Causes", "Prevention", "Treatment")

    val badgeColor = when (disease.category) {
        DiseaseCategory.MALIGNANT -> Color(0xFFD32F2F)
        DiseaseCategory.BENIGN -> Color(0xFF388E3C)
        DiseaseCategory.PRECANCEROUS -> Color(0xFFFF6F00)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan)
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
                    .padding(16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryNavy
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Hero Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (disease.category) {
                        DiseaseCategory.MALIGNANT -> Color(0xFFFFEBEE)
                        DiseaseCategory.BENIGN -> Color(0xFFE8F5E9)
                        DiseaseCategory.PRECANCEROUS -> Color(0xFFFFF3E0)
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = disease.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = badgeColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = when (disease.category) {
                                DiseaseCategory.MALIGNANT -> "⚠️ Malignant"
                                DiseaseCategory.BENIGN -> "✓ Benign"
                                DiseaseCategory.PRECANCEROUS -> "⚡ Precancerous"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // This is the tab Row, which is wrapped in white Card so that the labels are visible
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    edgePadding = 8.dp,
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = if (selectedTab == index) Color(0xFF1A237E) else Color(0xFF1A237E).copy(alpha = 0.4f)
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // This is the content based on selected tab
            when (selectedTab) {
                0 -> OverviewTab(disease)
                1 -> SymptomsTab(disease)
                2 -> CausesTab(disease)
                3 -> PreventionTab(disease)
                4 -> TreatmentTab(disease)
            }
        }
    }
}

@Composable
fun OverviewTab(disease: DiseaseInformation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardLight)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "What is ${disease.name}?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = disease.overview,
                fontSize = 15.sp,
                color = PrimaryNavy.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            if (disease.prognosis.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = PrimaryNavy.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Prognosis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = disease.prognosis,
                    fontSize = 14.sp,
                    color = PrimaryNavy.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
            }

            if (disease.faqs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = PrimaryNavy.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Frequently Asked Questions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                disease.faqs.forEach { faq ->
                    Text(
                        text = "Q: ${faq.question}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SecondaryBlue,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = "A: ${faq.answer}",
                        fontSize = 14.sp,
                        color = PrimaryNavy.copy(alpha = 0.8f),
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SymptomsTab(disease: DiseaseInformation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardLight)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Common Signs & Symptoms",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            disease.symptoms.forEach { symptom ->
                Row(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = symptom.icon,
                        fontSize = 18.sp,
                        color = SecondaryBlue,
                        modifier = Modifier.padding(end = 12.dp, top = 2.dp)
                    )
                    Column {
                        Text(
                            text = symptom.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = symptom.description,
                            fontSize = 14.sp,
                            color = PrimaryNavy.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            if (disease.whenToSeeDoctor.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = PrimaryNavy.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "⚠️ When to See a Doctor",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        disease.whenToSeeDoctor.forEach { warning ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = "•",
                                    fontSize = 14.sp,
                                    color = Color(0xFFC62828),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = warning,
                                    fontSize = 14.sp,
                                    color = PrimaryNavy.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CausesTab(disease: DiseaseInformation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardLight)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "What Causes ${disease.shortName}?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            disease.causes.forEach { cause ->
                Row(modifier = Modifier.padding(bottom = 12.dp)) {
                    Text(
                        text = "•",
                        fontSize = 16.sp,
                        color = SecondaryBlue,
                        modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                    )
                    Text(
                        text = cause,
                        fontSize = 15.sp,
                        color = PrimaryNavy.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }
            }

            if (disease.riskFactors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = PrimaryNavy.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Risk Factors",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                disease.riskFactors.forEach { factor ->
                    Row(modifier = Modifier.padding(bottom = 12.dp)) {
                        Text(
                            text = "•",
                            fontSize = 16.sp,
                            color = Color(0xFFFF6F00),
                            modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                        )
                        Text(
                            text = factor,
                            fontSize = 15.sp,
                            color = PrimaryNavy.copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PreventionTab(disease: DiseaseInformation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardLight)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "How to Prevent ${disease.shortName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            disease.prevention.forEach { step ->
                Row(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = "✓",
                        fontSize = 18.sp,
                        color = Color(0xFF388E3C),
                        modifier = Modifier.padding(end = 12.dp, top = 2.dp)
                    )
                    Text(
                        text = step,
                        fontSize = 15.sp,
                        color = PrimaryNavy.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TreatmentTab(disease: DiseaseInformation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardLight)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Treatment Options",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            disease.treatment.forEach { treatment ->
                Row(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = "•",
                        fontSize = 16.sp,
                        color = SecondaryBlue,
                        modifier = Modifier.padding(end = 12.dp, top = 2.dp)
                    )
                    Text(
                        text = treatment,
                        fontSize = 15.sp,
                        color = PrimaryNavy.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💡 Important Note",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Always consult a qualified dermatologist or healthcare provider for proper diagnosis and treatment. This information is for educational purposes only.",
                        fontSize = 14.sp,
                        color = PrimaryNavy.copy(alpha = 0.9f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}