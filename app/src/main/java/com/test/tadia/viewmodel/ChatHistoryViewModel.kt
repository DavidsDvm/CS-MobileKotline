package com.test.tadia.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.tadia.data.ChatHistory
import com.test.tadia.repository.FirestoreChatHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatHistoryUiState(
    val chats: List<ChatHistory> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ChatHistoryViewModel : ViewModel() {
    private val chatHistoryRepository = FirestoreChatHistoryRepository()
    
    private val _uiState = MutableStateFlow(ChatHistoryUiState())
    val uiState: StateFlow<ChatHistoryUiState> = _uiState.asStateFlow()
    
    fun loadChats(userId: String) {
        if (userId.isBlank()) {
            println("ChatHistoryViewModel: userId is blank, skipping load")
            return
        }
        
        println("ChatHistoryViewModel: Loading chats for userId: $userId")
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            try {
                val chats = chatHistoryRepository.getAllChats(userId)
                println("ChatHistoryViewModel: Loaded ${chats.size} chats")
                _uiState.value = _uiState.value.copy(
                    chats = chats,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                println("ChatHistoryViewModel: Error loading chats: ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    chats = emptyList(),
                    isLoading = false,
                    errorMessage = e.message ?: "Error desconocido al cargar chats"
                )
            }
        }
    }
    
    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            try {
                println("ChatHistoryViewModel: Deleting chat: $chatId")
                chatHistoryRepository.deleteChat(chatId)
                // Update UI immediately
                val currentChats = _uiState.value.chats
                val updatedChats = currentChats.filter { it.id != chatId }
                _uiState.value = _uiState.value.copy(
                    chats = updatedChats,
                    errorMessage = null
                )
                println("ChatHistoryViewModel: Chat deleted, remaining chats: ${updatedChats.size}")
            } catch (e: Exception) {
                println("ChatHistoryViewModel: Error deleting chat: ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Error al eliminar chat"
                )
            }
        }
    }
    
    fun refreshChats(userId: String) {
        println("ChatHistoryViewModel: Refresh requested for userId: $userId")
        loadChats(userId)
    }
}

