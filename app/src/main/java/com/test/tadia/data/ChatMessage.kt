package com.test.tadia.data

import java.util.*

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Date = Date()
)

