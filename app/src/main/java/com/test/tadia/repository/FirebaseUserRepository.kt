package com.test.tadia.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.test.tadia.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirebaseUserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun registerUser(email: String, password: String, name: String): Result<User> {
        return try {
            println("DEBUG: FirebaseUserRepository - Starting registration for: $email")
            // Create user in Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("User creation failed")
            println("DEBUG: FirebaseUserRepository - Auth user created: ${firebaseUser.uid}")
            
            // Create user document in Firestore
            val user = User(
                email = email,
                name = name,
                createdAt = Date()
            )
            
            usersCollection.document(email).set(user).await()
            println("DEBUG: FirebaseUserRepository - User document created in Firestore")
            
            Result.success(user)
        } catch (e: Exception) {
            println("DEBUG: FirebaseUserRepository - Registration failed: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            // Authenticate with Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Authentication failed")
            
            // Get user data from Firestore
            val document = usersCollection.document(email).get().await()
            val user = document.toObject(User::class.java) ?: throw Exception("User not found")
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {
        return try {
            val firebaseUser = auth.currentUser ?: return null
            val document = usersCollection.document(firebaseUser.email ?: return null).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun logout() {
        auth.signOut()
    }

    suspend fun deleteUser(): Result<Unit> {
        return try {
            val firebaseUser = auth.currentUser ?: throw Exception("No user logged in")
            
            // Delete from Firestore
            usersCollection.document(firebaseUser.email ?: "").delete().await()
            
            // Delete from Auth
            firebaseUser.delete().await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}
