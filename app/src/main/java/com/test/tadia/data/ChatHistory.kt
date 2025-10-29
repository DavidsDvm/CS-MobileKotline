package com.test.tadia.data

import java.util.Date
import java.util.UUID

/**
 * Represents a chat conversation with the TadIA chatbot
 */
data class ChatHistory(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val userId: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Date = Date(),
    val createdAt: Date = Date(),
    val messageCount: Int = 0
)

