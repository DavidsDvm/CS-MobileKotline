package com.test.tadia.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.tadia.utils.ImageUploadManager
import kotlinx.coroutines.launch

data class ImageUploadState(
    val isUploading: Boolean = false,
    val uploadProgress: Float = 0f,
    val uploadedImageUrl: String? = null,
    val errorMessage: String? = null
)

class ImageUploadViewModel : ViewModel() {
    private val imageUploadManager = ImageUploadManager()
    
    private val _uiState = mutableStateOf(ImageUploadState())
    val uiState: ImageUploadState get() = _uiState.value

    fun uploadImage(uri: Uri, newsId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isUploading = true,
                errorMessage = null,
                uploadProgress = 0f
            )
            
            try {
                val imageUrl = imageUploadManager.uploadImage(uri, newsId)
                if (imageUrl != null) {
                    _uiState.value = _uiState.value.copy(
                        isUploading = false,
                        uploadedImageUrl = imageUrl,
                        uploadProgress = 1f
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isUploading = false,
                        errorMessage = "Error al subir la imagen"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    errorMessage = "Error al subir la imagen: ${e.message}"
                )
            }
        }
    }

    fun clearState() {
        _uiState.value = ImageUploadState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
