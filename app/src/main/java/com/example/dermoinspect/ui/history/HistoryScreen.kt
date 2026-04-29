// Janaath Vijithavarnan
// W1979142

// This file contains the UI for displaying the user's scan history.

package com.example.dermoinspect.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dermoinspect.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.dermoinspect.data.model.ScanHistory



// The HistoryScreen composable shows a list of all previous skin scans performed by the user
// It manages different UI states including loading, error, and success states with appropriate messages and UI elements for each


@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = viewModel(),
            onScanClick: (ScanHistory) -> Unit = {}
) {
    val historyState by viewModel.historyState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan)
            .padding(16.dp)
    ) {
        // This is the Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )

            // Refresh button
            IconButton(onClick = { viewModel.loadHistory() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = PrimaryNavy
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        when (val state = historyState) {
            is HistoryState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SecondaryBlue)
                }
            }

            is HistoryState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading history",
                            fontSize = 18.sp,
                            color = PrimaryNavy
                        )
                        Text(
                            text = state.message,
                            fontSize = 14.sp,
                            color = PrimaryNavy.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadHistory() }) {
                            Text("Retry")
                        }
                    }
                }
            }

            is HistoryState.Success -> {
                if (state.scans.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "No History",
                                tint = PrimaryNavy.copy(alpha = 0.3f),
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No scan history yet",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = PrimaryNavy
                            )
                            Text(
                                text = "Your scan results will appear here",
                                fontSize = 16.sp,
                                color = PrimaryNavy.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    // This the history list
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.scans) { scan ->
                            HistoryCard(
                                scan = scan,
                                onDelete = { showDeleteDialog = scan.id },
                                onClick = { onScanClick(scan) }
                            )
                        }
                    }
                }
            }
        }
    }

// This is a delete dialog to show that it has been deleted
    showDeleteDialog?.let { scanId ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Scan?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteScan(scanId)
                        showDeleteDialog = null
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}


// The HistoryCard composable displays individual scan records in a card format, showing a little thumbnail of the scanned image,
// the detected disease type, confidence percentage, and scan date. Users can tap on any scan to view details or use the delete button
// to remove scans from their history.

@Composable
fun HistoryCard(
    scan: com.example.dermoinspect.data.model.ScanHistory,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Image
            AsyncImage(
                model = scan.imageUrl,
                contentDescription = "Scan image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Disease name
                Text(
                    text = scan.diseaseType,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )

                // Confidence
                Text(
                    text = "${(scan.confidence * 100).toInt()}% confidence",
                    fontSize = 14.sp,
                    color = SecondaryBlue
                )

                // Malignant badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (scan.isMalignant)
                        Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = if (scan.isMalignant) "Malignant" else "Benign",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (scan.isMalignant)
                            Color(0xFFD32F2F) else Color(0xFF388E3C),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Date
                Text(
                    text = formatDate(scan.timestamp),
                    fontSize = 12.sp,
                    color = PrimaryNavy.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Delete button
            // A confirmation dialog appears before deleting to prevent accidental removals.

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }
        }
    }
}


// The formatDate function converts timestamps into a readable date format for display.


fun formatDate(date: Date): String {
    val now = Date()
    val diff = now.time - date.time

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000}m ago"
        diff < 86400000 -> "${diff / 3600000}h ago"
        diff < 604800000 -> "${diff / 86400000}d ago"
        else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
    }
}


