package com.test.tadia.data

import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Firebase Realtime Database service for handling database operations
 */
class FirebaseService {
    
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    
    init {
        // Set the database URL (you can also set this in google-services.json)
        database.setPersistenceEnabled(true) // Enable offline persistence
    }
    
    /**
     * Write data to a specific path in the database
     * @param path The path where to write the data
     * @param data The data to write
     */
    suspend fun writeData(path: String, data: Any) {
        try {
            database.getReference(path).setValue(data).await()
        } catch (e: Exception) {
            throw FirebaseException("Failed to write data: ${e.message}", e)
        }
    }
    
    /**
     * Read data once from a specific path
     * @param path The path to read from
     * @return The data at the specified path
     */
    suspend fun <T> readData(path: String, clazz: Class<T>): T? {
        return try {
            val snapshot = database.getReference(path).get().await()
            snapshot.getValue(clazz)
        } catch (e: Exception) {
            throw FirebaseException("Failed to read data: ${e.message}", e)
        }
    }
    
    /**
     * Listen to real-time changes at a specific path
     * @param path The path to listen to
     * @return Flow of data changes
     */
    fun <T> listenToData(path: String, clazz: Class<T>): Flow<T?> = callbackFlow {
        val reference = database.getReference(path)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val data = snapshot.getValue(clazz)
                    trySend(data)
                } catch (e: Exception) {
                    close(e)
                }
            }
            
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        reference.addValueEventListener(listener)
        
        awaitClose {
            reference.removeEventListener(listener)
        }
    }
    
    /**
     * Update specific fields in the database
     * @param path The path to update
     * @param updates Map of field paths to new values
     */
    suspend fun updateData(path: String, updates: Map<String, Any>) {
        try {
            database.getReference(path).updateChildren(updates).await()
        } catch (e: Exception) {
            throw FirebaseException("Failed to update data: ${e.message}", e)
        }
    }
    
    /**
     * Delete data at a specific path
     * @param path The path to delete
     */
    suspend fun deleteData(path: String) {
        try {
            database.getReference(path).removeValue().await()
        } catch (e: Exception) {
            throw FirebaseException("Failed to delete data: ${e.message}", e)
        }
    }
    
    /**
     * Push data to a list (generates unique key)
     * @param path The path where to push the data
     * @param data The data to push
     * @return The generated key
     */
    suspend fun pushData(path: String, data: Any): String? {
        return try {
            val reference = database.getReference(path).push()
            reference.setValue(data).await()
            reference.key
        } catch (e: Exception) {
            throw FirebaseException("Failed to push data: ${e.message}", e)
        }
    }
    
    /**
     * Query data with specific conditions
     * @param path The path to query
     * @param orderBy The field to order by
     * @param limitToFirst Number of items to limit (optional)
     * @param limitToLast Number of items to limit from the end (optional)
     * @return List of matching data
     */
    suspend fun <T> queryData(
        path: String, 
        clazz: Class<T>,
        orderBy: String? = null,
        limitToFirst: Int? = null,
        limitToLast: Int? = null
    ): List<T> {
        return try {
            var query: Query = database.getReference(path)
            
            orderBy?.let { query = query.orderByChild(it) }
            limitToFirst?.let { query = query.limitToFirst(it) }
            limitToLast?.let { query = query.limitToLast(it) }
            
            val snapshot = query.get().await()
            val result = mutableListOf<T>()
            
            for (child in snapshot.children) {
                child.getValue(clazz)?.let { result.add(it) }
            }
            
            result
        } catch (e: Exception) {
            throw FirebaseException("Failed to query data: ${e.message}", e)
        }
    }
}

/**
 * Custom exception for Firebase operations
 */
class FirebaseException(message: String, cause: Throwable? = null) : Exception(message, cause)
