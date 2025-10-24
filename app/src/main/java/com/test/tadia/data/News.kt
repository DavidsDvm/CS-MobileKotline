package com.test.tadia.data

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class News(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val summary: String? = null, // Optional for complex news
    val imageUrl: String? = null, // URL to uploaded image in Firebase Storage
    val type: NewsType = NewsType.SIMPLE,
    val keywords: List<String> = emptyList(),
    val authorName: String = "",
    val authorEmail: String = "",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isPublished: Boolean = true,
    val publishedAt: Date? = null
)

enum class NewsType {
    SIMPLE,
    COMPLEX
}

// Extension functions for News
fun News.canUserEdit(userEmail: String): Boolean {
    return this.authorEmail == userEmail
}

fun News.getFormattedDate(): String {
    val formatter = java.text.SimpleDateFormat("dd 'de' MMMM 'de' yyyy", java.util.Locale("es", "ES"))
    return formatter.format(createdAt)
}

fun News.getRelativeTime(): String {
    val now = Date()
    val diffInSeconds = (now.time - createdAt.time) / 1000
    
    return when {
        diffInSeconds < 60 -> "Hace unos segundos"
        diffInSeconds < 3600 -> "Hace ${diffInSeconds / 60} minutos"
        diffInSeconds < 86400 -> "Hace ${diffInSeconds / 3600} horas"
        diffInSeconds < 604800 -> "Hace ${diffInSeconds / 86400} días"
        else -> getFormattedDate()
    }
}

// Helper function to create a new News item
fun createNews(
    id: String = System.currentTimeMillis().toString(),
    title: String,
    description: String,
    summary: String? = null,
    imageUrl: String? = null,
    type: NewsType = NewsType.SIMPLE,
    keywords: List<String> = emptyList(),
    authorName: String,
    authorEmail: String,
    isPublished: Boolean = true
): News {
    val now = Date()
    return News(
        id = id,
        title = title,
        description = description,
        summary = summary,
        imageUrl = imageUrl,
        type = type,
        keywords = keywords,
        authorName = authorName,
        authorEmail = authorEmail,
        createdAt = now,
        updatedAt = now,
        isPublished = isPublished,
        publishedAt = if (isPublished) now else null
    )
}
