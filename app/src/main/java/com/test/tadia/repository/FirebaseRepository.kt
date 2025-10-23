package com.test.tadia.repository

import com.test.tadia.data.FirebaseService
import com.test.tadia.data.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class for Firebase operations
 * This class provides a clean interface for Firebase database operations
 */
@Singleton
class FirebaseRepository @Inject constructor(
    private val firebaseService: FirebaseService
) {
    
    // User operations
    suspend fun createUser(user: User): String? {
        return firebaseService.pushData("users", user)
    }
    
    suspend fun getUser(userId: String): User? {
        return firebaseService.readData("users/$userId", User::class.java)
    }
    
    suspend fun updateUser(userId: String, user: User) {
        firebaseService.writeData("users/$userId", user)
    }
    
    suspend fun deleteUser(userId: String) {
        firebaseService.deleteData("users/$userId")
    }
    
    fun listenToUser(userId: String): Flow<User?> {
        return firebaseService.listenToData("users/$userId", User::class.java)
    }
    
    suspend fun getAllUsers(): List<User> {
        return firebaseService.queryData("users", User::class.java)
    }
    
    // Generic operations for any data type
    suspend fun <T> writeData(path: String, data: Any) {
        firebaseService.writeData(path, data)
    }
    
    suspend fun <T> readData(path: String, clazz: Class<T>): T? {
        return firebaseService.readData(path, clazz)
    }
    
    suspend fun updateData(path: String, updates: Map<String, Any>) {
        firebaseService.updateData(path, updates)
    }
    
    suspend fun deleteData(path: String) {
        firebaseService.deleteData(path)
    }
    
    fun <T> listenToData(path: String, clazz: Class<T>): Flow<T?> {
        return firebaseService.listenToData(path, clazz)
    }
    
    suspend fun <T> queryData(
        path: String,
        clazz: Class<T>,
        orderBy: String? = null,
        limitToFirst: Int? = null,
        limitToLast: Int? = null
    ): List<T> {
        return firebaseService.queryData(path, clazz, orderBy, limitToFirst, limitToLast)
    }
}
