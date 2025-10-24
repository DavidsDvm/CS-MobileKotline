package com.test.tadia.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.test.tadia.data.News
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirebaseNewsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val newsCollection = firestore.collection("news")

    suspend fun addNews(news: News) {
        newsCollection.document(news.id).set(news).await()
    }

    suspend fun updateNews(news: News) {
        val updatedNews = news.copy(updatedAt = Date())
        newsCollection.document(news.id).set(updatedNews).await()
    }

    suspend fun deleteNews(newsId: String) {
        newsCollection.document(newsId).delete().await()
    }

    suspend fun getNewsById(newsId: String): News? {
        val document = newsCollection.document(newsId).get().await()
        return document.toObject(News::class.java)
    }

    suspend fun getAllNews(): List<News> {
        val snapshot = newsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(News::class.java) }
    }

    suspend fun getPublishedNews(): List<News> {
        val snapshot = newsCollection
            .whereEqualTo("isPublished", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(News::class.java) }
    }

    suspend fun getNewsByAuthor(authorEmail: String): List<News> {
        val snapshot = newsCollection
            .whereEqualTo("authorEmail", authorEmail)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(News::class.java) }
    }

    fun getAllNewsFlow(): Flow<List<News>> = flow {
        newsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                
                val news = snapshot?.documents?.mapNotNull { 
                    it.toObject(News::class.java) 
                } ?: emptyList()
                
                // Note: This is a simplified approach. In a real app, you'd want to use
                // a proper Flow implementation that can handle the listener lifecycle
            }
    }

    fun getPublishedNewsFlow(): Flow<List<News>> = flow {
        newsCollection
            .whereEqualTo("isPublished", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                
                val news = snapshot?.documents?.mapNotNull { 
                    it.toObject(News::class.java) 
                } ?: emptyList()
                
                // Note: This is a simplified approach. In a real app, you'd want to use
                // a proper Flow implementation that can handle the listener lifecycle
            }
    }

    fun getNewsByAuthorFlow(authorEmail: String): Flow<List<News>> = flow {
        newsCollection
            .whereEqualTo("authorEmail", authorEmail)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                
                val news = snapshot?.documents?.mapNotNull { 
                    it.toObject(News::class.java) 
                } ?: emptyList()
                
                // Note: This is a simplified approach. In a real app, you'd want to use
                // a proper Flow implementation that can handle the listener lifecycle
            }
    }

    suspend fun searchNews(query: String): List<News> {
        val snapshot = newsCollection
            .whereEqualTo("isPublished", true)
            .get()
            .await()
        
        return snapshot.documents.mapNotNull { it.toObject(News::class.java) }
            .filter { news ->
                news.title.contains(query, ignoreCase = true) ||
                news.description.contains(query, ignoreCase = true) ||
                news.summary?.contains(query, ignoreCase = true) == true ||
                news.keywords.any { keyword -> keyword.contains(query, ignoreCase = true) }
            }
    }

    suspend fun deleteAllNews() {
        val batch = firestore.batch()
        val snapshot = newsCollection.get().await()
        
        snapshot.documents.forEach { document ->
            batch.delete(document.reference)
        }
        
        batch.commit().await()
    }
}
