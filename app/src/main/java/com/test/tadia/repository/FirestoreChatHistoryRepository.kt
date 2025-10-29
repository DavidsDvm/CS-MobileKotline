package com.test.tadia.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.test.tadia.data.ChatHistory
import com.test.tadia.data.ChatMessage
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

/**
 * Repository for managing chat history with Firestore
 */
class FirestoreChatHistoryRepository {
    
    private val db = FirebaseFirestore.getInstance()
    private val CHATS_COLLECTION = "chat-history"
    private val MESSAGES_SUBCOLLECTION = "messages"
    
    /**
     * Create a new chat and return its ID
     */
    suspend fun createNewChat(userId: String, title: String = "Nueva conversación"): String {
        return try {
            val chat = ChatHistory(
                id = UUID.randomUUID().toString(),
                title = title,
                userId = userId,
                lastMessage = "",
                createdAt = Date(),
                lastMessageTime = Date(),
                messageCount = 0
            )
            
            db.collection(CHATS_COLLECTION).document(chat.id)
                .set(chat)
                .await()
            
            chat.id
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al crear el chat: ${e.message}")
        }
    }
    
    /**
     * Save a message to a chat
     */
    suspend fun saveMessage(chatId: String, message: ChatMessage, chatTitle: String? = null) {
        try {
            // Save the message
            db.collection(CHATS_COLLECTION)
                .document(chatId)
                .collection(MESSAGES_SUBCOLLECTION)
                .document(message.id)
                .set(message)
                .await()
            
            // Update chat metadata
            val updateData = hashMapOf<String, Any>(
                "lastMessage" to message.text,
                "lastMessageTime" to message.timestamp,
                "messageCount" to com.google.firebase.firestore.FieldValue.increment(1)
            )
            
            // Update title if provided and chat is new (title is still default)
            chatTitle?.let { title ->
                val chatDoc = db.collection(CHATS_COLLECTION).document(chatId).get().await()
                val currentTitle = chatDoc.getString("title") ?: ""
                if (currentTitle == "Nueva conversación" || currentTitle.isEmpty()) {
                    updateData["title"] = title
                }
            }
            
            db.collection(CHATS_COLLECTION)
                .document(chatId)
                .update(updateData)
                .await()
            
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al guardar mensaje: ${e.message}")
        }
    }
    
    /**
     * Get all chats for a user, ordered by last message time
     */
    suspend fun getAllChats(userId: String): List<ChatHistory> {
        if (userId.isBlank()) {
            println("FirestoreChatHistoryRepository: userId is blank, returning empty list")
            return emptyList()
        }
        
        return try {
            println("FirestoreChatHistoryRepository: Querying chats for userId: $userId")
            
            // Try with orderBy first (requires composite index)
            val snapshot = try {
                db.collection(CHATS_COLLECTION)
                    .whereEqualTo("userId", userId)
                    .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                    .get()
                    .await()
            } catch (e: Exception) {
                // If orderBy fails (e.g., missing index), try without orderBy
                println("FirestoreChatHistoryRepository: orderBy failed, trying without orderBy: ${e.message}")
                db.collection(CHATS_COLLECTION)
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()
            }
            
            println("FirestoreChatHistoryRepository: Found ${snapshot.documents.size} documents")
            
            val chats = snapshot.documents.mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null
                    
                    val chat = ChatHistory(
                        id = doc.id,
                        title = data["title"] as? String ?: "Sin título",
                        userId = data["userId"] as? String ?: "",
                        lastMessage = data["lastMessage"] as? String ?: "",
                        lastMessageTime = (data["lastMessageTime"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
                        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
                        messageCount = (data["messageCount"] as? Long)?.toInt() ?: 0
                    )
                    
                    println("FirestoreChatHistoryRepository: Parsed chat: id=${chat.id}, title=${chat.title}")
                    chat
                } catch (e: Exception) {
                    println("FirestoreChatHistoryRepository: Error parsing document ${doc.id}: ${e.message}")
                    e.printStackTrace()
                    null
                }
            }.sortedByDescending { it.lastMessageTime }
            
            println("FirestoreChatHistoryRepository: Returning ${chats.size} chats")
            chats
        } catch (e: Exception) {
            println("FirestoreChatHistoryRepository: Error getting chats: ${e.message}")
            e.printStackTrace()
            // Re-throw to allow ViewModel to handle it
            throw Exception("Error al obtener chats: ${e.message}")
        }
    }
    
    /**
     * Get all messages from a specific chat
     */
    suspend fun getChatMessages(chatId: String): List<ChatMessage> {
        return try {
            val snapshot = db.collection(CHATS_COLLECTION)
                .document(chatId)
                .collection(MESSAGES_SUBCOLLECTION)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null
                    
                    val isUser = data["isUser"] as? Boolean ?: false
                    val text = data["text"] as? String ?: ""
                    
                    println("FirestoreChatHistoryRepository: Loading message - isUser=$isUser, text=${text.take(50)}")
                    
                    ChatMessage(
                        id = doc.id,
                        text = text,
                        isUser = isUser,
                        timestamp = (data["timestamp"] as? com.google.firebase.Timestamp)?.toDate() ?: Date()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Update chat title
     */
    suspend fun updateChatTitle(chatId: String, title: String) {
        try {
            db.collection(CHATS_COLLECTION)
                .document(chatId)
                .update("title", title)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al actualizar título: ${e.message}")
        }
    }
    
    /**
     * Delete a chat
     */
    suspend fun deleteChat(chatId: String) {
        try {
            // Delete all messages first
            val messagesSnapshot = db.collection(CHATS_COLLECTION)
                .document(chatId)
                .collection(MESSAGES_SUBCOLLECTION)
                .get()
                .await()
            
            messagesSnapshot.documents.forEach { doc ->
                doc.reference.delete().await()
            }
            
            // Delete the chat
            db.collection(CHATS_COLLECTION)
                .document(chatId)
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al eliminar chat: ${e.message}")
        }
    }
}

