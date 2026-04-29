// Janaath Vijithavarnan
// W1979142

package com.example.dermoinspect.ui.information

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermoinspect.data.DiseaseDatabase
import com.example.dermoinspect.data.model.DiseaseCategory
import com.example.dermoinspect.data.model.DiseaseInformation
import com.example.dermoinspect.ui.theme.*


@Composable
fun InformationScreen(
    onDiseaseClick: (DiseaseInformation) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<DiseaseCategory?>(null) }

    val allDiseases = remember { DiseaseDatabase.getAllDiseases() }

    val filteredDiseases = remember(searchQuery, selectedCategory) {
        allDiseases.filter { disease ->
            val matchesSearch = disease.name.contains(searchQuery, ignoreCase = true) ||
                    disease.shortDescription.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == null || disease.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Information",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryNavy,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Learn About Skin Conditions",
            fontSize = 16.sp,
            color = PrimaryNavy.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Search diseases, symptoms...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CardLight,
                unfocusedContainerColor = CardLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        // Category Filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryChip(
                label = "All",
                isSelected = selectedCategory == null,
                onClick = { selectedCategory = null }
            )
            CategoryChip(
                label = "Malignant",
                isSelected = selectedCategory == DiseaseCategory.MALIGNANT,
                onClick = { selectedCategory = DiseaseCategory.MALIGNANT }
            )
            CategoryChip(
                label = "Benign",
                isSelected = selectedCategory == DiseaseCategory.BENIGN,
                onClick = { selectedCategory = DiseaseCategory.BENIGN }
            )
            CategoryChip(
                label = "Pre-cancer",
                isSelected = selectedCategory == DiseaseCategory.PRECANCEROUS,
                onClick = { selectedCategory = DiseaseCategory.PRECANCEROUS }
            )
        }

        // Disease Grid
        if (filteredDiseases.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No diseases found",
                    color = PrimaryNavy.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredDiseases) { disease ->
                    DiseaseCard(
                        disease = disease,
                        onClick = { onDiseaseClick(disease) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = CardLight,
            selectedContainerColor = SecondaryBlue,
            labelColor = PrimaryNavy,
            selectedLabelColor = Color.White
        )
    )
}

// card of the disease
@Composable
fun DiseaseCard(
    disease: DiseaseInformation,
    onClick: () -> Unit
) {
    val backgroundColor = when (disease.category) {
        DiseaseCategory.MALIGNANT -> Color(0xFFFFEBEE)
        DiseaseCategory.BENIGN -> Color(0xFFE8F5E9)
        DiseaseCategory.PRECANCEROUS -> Color(0xFFFFF3E0)
    }

    val badgeColor = when (disease.category) {
        DiseaseCategory.MALIGNANT -> Color(0xFFD32F2F)
        DiseaseCategory.BENIGN -> Color(0xFF388E3C)
        DiseaseCategory.PRECANCEROUS -> Color(0xFFFF6F00)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = when (disease.category) {
                            DiseaseCategory.MALIGNANT -> "⚠️ Malignant"
                            DiseaseCategory.BENIGN -> "✓ Benign"
                            DiseaseCategory.PRECANCEROUS -> "⚡ Precancerous"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Disease name
                Text(
                    text = disease.shortName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    maxLines = 2
                )
            }

            // Learn more
            Text(
                text = "Learn more →",
                fontSize = 13.sp,
                color = SecondaryBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

