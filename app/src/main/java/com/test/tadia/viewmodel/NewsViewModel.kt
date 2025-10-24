package com.test.tadia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.tadia.data.*
import com.test.tadia.data.canUserEdit
import com.test.tadia.repository.FirebaseNewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NewsUiState(
    val news: List<News> = emptyList(),
    val selectedNews: News? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val operationSuccessful: Boolean = false,
    val isCreating: Boolean = false,
    val isEditing: Boolean = false
)

class NewsViewModel(
    private val newsRepository: FirebaseNewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()
    
    private var currentUserEmail: String? = null

    fun setCurrentUser(userEmail: String) {
        currentUserEmail = userEmail
    }

    fun selectNews(news: News) {
        _uiState.value = _uiState.value.copy(selectedNews = news)
    }

    fun createNews(
        title: String,
        description: String,
        summary: String? = null,
        imageUrl: String? = null,
        type: NewsType = NewsType.SIMPLE,
        keywords: List<String> = emptyList(),
        authorName: String,
        isPublished: Boolean = true
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                errorMessage = null, 
                isLoading = true,
                isCreating = true
            )
            
            try {
                val news = createNews(
                    id = System.currentTimeMillis().toString(),
                    title = title,
                    description = description,
                    summary = summary,
                    imageUrl = imageUrl,
                    type = type,
                    keywords = keywords,
                    authorName = authorName,
                    authorEmail = currentUserEmail ?: "",
                    isPublished = isPublished
                )
                
                newsRepository.addNews(news)
                loadAllNews()
                _uiState.value = _uiState.value.copy(
                    errorMessage = null, 
                    isLoading = false, 
                    operationSuccessful = true,
                    isCreating = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al crear la noticia: ${e.message}",
                    isLoading = false,
                    isCreating = false
                )
            }
        }
    }

    fun updateNews(news: News) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                errorMessage = null,
                isEditing = true
            )
            
            // Check if user can edit this news
            if (!news.canUserEdit(currentUserEmail ?: "")) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "No tienes permisos para editar esta noticia",
                    isLoading = false,
                    isEditing = false
                )
                return@launch
            }
            
            try {
                newsRepository.updateNews(news)
                loadAllNews()
                _uiState.value = _uiState.value.copy(
                    errorMessage = null, 
                    isLoading = false, 
                    operationSuccessful = true,
                    isEditing = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar la noticia: ${e.message}",
                    isLoading = false,
                    isEditing = false
                )
            }
        }
    }

    fun deleteNews(newsId: String) {
        viewModelScope.launch {
            val news = newsRepository.getNewsById(newsId)
            if (news != null && !news.canUserEdit(currentUserEmail ?: "")) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "No tienes permisos para eliminar esta noticia"
                )
                return@launch
            }
            
            try {
                newsRepository.deleteNews(newsId)
                loadAllNews()
                _uiState.value = _uiState.value.copy(
                    selectedNews = null, 
                    errorMessage = null,
                    operationSuccessful = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar la noticia: ${e.message}"
                )
            }
        }
    }

    fun loadAllNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val news = newsRepository.getAllNews()
                _uiState.value = _uiState.value.copy(
                    news = news,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar las noticias: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadPublishedNews() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val news = newsRepository.getPublishedNews()
                _uiState.value = _uiState.value.copy(
                    news = news,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar las noticias: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadNewsByAuthor() {
        currentUserEmail?.let { email ->
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)
                try {
                    val news = newsRepository.getNewsByAuthor(email)
                    _uiState.value = _uiState.value.copy(
                        news = news,
                        isLoading = false
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error al cargar tus noticias: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val news = newsRepository.searchNews(query)
                _uiState.value = _uiState.value.copy(
                    news = news,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al buscar noticias: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedNews = null)
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun clearSuccessFlag() {
        _uiState.value = _uiState.value.copy(operationSuccessful = false)
    }
}
