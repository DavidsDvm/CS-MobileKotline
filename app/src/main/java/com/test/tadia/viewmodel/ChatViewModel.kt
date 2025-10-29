package com.test.tadia.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.tadia.data.ChatMessage
import com.test.tadia.repository.FirestoreChatHistoryRepository
import com.test.tadia.service.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val chatHistoryId: String? = null,
    val isNewChat: Boolean = true
)

class ChatViewModel : ViewModel() {
    private val geminiService: GeminiService? = try {
        GeminiService()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    
    private val chatHistoryRepository = FirestoreChatHistoryRepository()
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    fun sendMessage(messageText: String, userId: String = "") {
        if (messageText.isBlank()) return
        
        val currentChatId = _uiState.value.chatHistoryId
        val isNewChat = _uiState.value.isNewChat
        
        // Add user message
        val userMessage = ChatMessage(
            text = messageText,
            isUser = true
        )
        
        // Save previous messages BEFORE updating state (for conversation history)
        val previousMessages = _uiState.value.messages
        
        val currentMessages = previousMessages + userMessage
        _uiState.value = _uiState.value.copy(
            messages = currentMessages,
            isLoading = true,
            errorMessage = null
        )
        
        // Get AI response
        viewModelScope.launch {
            if (geminiService == null) {
                val errorBotMessage = ChatMessage(
                    text = "Servicio de chat no disponible. Por favor, verifica tu conexión a internet.",
                    isUser = false
                )
                _uiState.value = _uiState.value.copy(
                    messages = currentMessages + errorBotMessage,
                    isLoading = false
                )
                return@launch
            }
            
            try {
                // Build conversation history for context
                // Use previousMessages (before adding current user message)
                val conversationHistory = mutableListOf<Pair<String, String>>()
                
                // Pair consecutive user-bot messages from previous conversation
                var i = 0
                while (i < previousMessages.size - 1) {
                    val currentMsg = previousMessages[i]
                    val nextMsg = previousMessages[i + 1]
                    
                    if (currentMsg.isUser && !nextMsg.isUser) {
                        conversationHistory.add(currentMsg.text to nextMsg.text)
                        i += 2 // Skip both messages
                    } else {
                        i++
                    }
                }
                
                val response = geminiService.getChatResponse(messageText, conversationHistory)
                val botMessage = ChatMessage(
                    text = response,
                    isUser = false
                )
                
                _uiState.value = _uiState.value.copy(
                    messages = currentMessages + botMessage,
                    isLoading = false
                )
                
                // Save messages to history
                val chatId = if (currentChatId == null && userId.isNotBlank()) {
                    // Create new chat
                    val newChatId = chatHistoryRepository.createNewChat(userId, generateTitle(messageText))
                    _uiState.value = _uiState.value.copy(
                        chatHistoryId = newChatId,
                        isNewChat = false // Chat is no longer new after first message
                    )
                    newChatId
                } else {
                    currentChatId
                }
                
                // Save both messages to Firestore
                chatId?.let { id ->
                    chatHistoryRepository.saveMessage(id, userMessage, generateTitle(messageText))
                    chatHistoryRepository.saveMessage(id, botMessage, null)
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                val errorBotMessage = ChatMessage(
                    text = "Lo siento, hubo un problema al procesar tu solicitud. Por favor, intenta nuevamente.",
                    isUser = false
                )
                
                _uiState.value = _uiState.value.copy(
                    messages = currentMessages + errorBotMessage,
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    /**
     * Start a new chat - saves current chat first if it has messages
     * This is a suspend function to ensure the save completes before starting new chat
     */
    suspend fun startNewChat(userId: String = "") {
        val currentChatId = _uiState.value.chatHistoryId
        val currentMessages = _uiState.value.messages
        
        // Save current chat if it has messages
        if (currentMessages.isNotEmpty() && userId.isNotBlank()) {
            try {
                if (currentChatId != null) {
                    // Chat exists - ensure last message is saved to update lastMessageTime
                    // This ensures the chat appears properly sorted in history
                    currentMessages.lastOrNull()?.let { lastMessage ->
                        chatHistoryRepository.saveMessage(currentChatId, lastMessage, null)
                    }
                } else {
                    // No chat ID but has messages - create chat and save all
                    val firstUserMessage = currentMessages.firstOrNull { it.isUser }
                    if (firstUserMessage != null) {
                        val newChatId = chatHistoryRepository.createNewChat(
                            userId, 
                            generateTitle(firstUserMessage.text)
                        )
                        
                        // Save all messages to the new chat
                        currentMessages.forEach { message ->
                            chatHistoryRepository.saveMessage(
                                newChatId, 
                                message, 
                                if (message == firstUserMessage) generateTitle(message.text) else null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Continue anyway to start new chat
            }
        }
        
        // Start fresh chat
        _uiState.value = ChatUiState(
            chatHistoryId = null,
            isNewChat = true,
            messages = listOf()
        )
    }
    
    /**
     * Load an existing chat
     */
    fun loadChat(chatId: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            chatHistoryId = chatId,
            isNewChat = false
        )
        
        viewModelScope.launch {
            try {
                val messages = chatHistoryRepository.getChatMessages(chatId)
                _uiState.value = _uiState.value.copy(
                    messages = messages,
                    isLoading = false,
                    chatHistoryId = chatId,
                    isNewChat = false
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    /**
     * Generate a title for the chat based on first message
     */
    private fun generateTitle(firstMessage: String): String {
        // Simple title generation - take first few words or truncate
        val words = firstMessage.split(" ")
        return if (words.size > 5) {
            words.take(5).joinToString(" ") + "..."
        } else {
            firstMessage.take(30)
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

