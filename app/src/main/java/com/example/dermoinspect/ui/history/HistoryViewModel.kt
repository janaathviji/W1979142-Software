// Janaath Vijithavarnan
// W1979142

// This file manages the data and logic for the scan history screen.
package com.example.dermoinspect.ui.history

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dermoinspect.data.model.ScanHistory
import com.example.dermoinspect.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// HistoryState tracks whether the history list is loading, loaded successfully, or
// encountered an error. SaveState tracks the status of saving new scans.
sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(val scans: List<ScanHistory>) : HistoryState()
    data class Error(val message: String) : HistoryState()
}

sealed class SaveState {
    object Idle : SaveState()
    object Saving : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
}


// The ViewModel provides three main functions like loadHistory fetches all scans from the repository,
// saveScan stores a new scan result with its image and predictions to Firebase, and deleteScan removes
// a scan from the database.

class HistoryViewModel : ViewModel() {

    private val repository = HistoryRepository()

    private val _historyState = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val historyState: StateFlow<HistoryState> = _historyState.asStateFlow()

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _historyState.value = HistoryState.Loading

            val result = repository.getScanHistory()

            _historyState.value = result.fold(
                onSuccess = { HistoryState.Success(it) },
                onFailure = { HistoryState.Error(it.message ?: "Failed to load history") }
            )
        }
    }

    fun saveScan(
        context: Context,
        imageUri: Uri,
        diseaseType: String,
        confidence: Float,
        topPredictions: List<Pair<String, Float>>,
        isMalignant: Boolean
    ) {
        viewModelScope.launch {
            _saveState.value = SaveState.Saving

            val result = repository.saveScanResult(
                context = context,
                imageUri = imageUri,
                diseaseType = diseaseType,
                confidence = confidence,
                topPredictions = topPredictions,
                isMalignant = isMalignant
            )

            _saveState.value = result.fold(
                onSuccess = {
                    loadHistory() // Refresh history
                    SaveState.Success
                },
                onFailure = { SaveState.Error(it.message ?: "Failed to save") }
            )
        }
    }

    fun deleteScan(scanId: String) {
        viewModelScope.launch {
            val result = repository.deleteScan(scanId)

            if (result.isSuccess) {
                loadHistory() // This is to refresh history
            }
        }
    }

}
