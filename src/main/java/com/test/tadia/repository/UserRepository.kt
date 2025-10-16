package com.test.tadia.repository

import com.test.tadia.data.AppDatabase
import com.test.tadia.data.User
import com.test.tadia.utils.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val database: AppDatabase) {
    private val userDao = database.userDao()

    suspend fun registerUser(email: String, name: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                // Check if user already exists
                val existingUser = userDao.getUserByEmail(email)
                if (existingUser != null) {
                    return@withContext Result.failure(Exception("User with this email already exists"))
                }

                // Validate password
                if (!PasswordUtils.isPasswordValid(password)) {
                    return@withContext Result.failure(Exception("Password does not meet requirements"))
                }

                // Hash password and create user
                val hashedPassword = PasswordUtils.hashPassword(password)
                val user = User(
                    email = email.trim().lowercase(),
                    name = name.trim(),
                    passwordHash = hashedPassword
                )

                userDao.insertUser(user)
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val user = userDao.getUserByEmail(email.trim().lowercase())
                if (user == null) {
                    return@withContext Result.failure(Exception("User not found"))
                }

                if (PasswordUtils.verifyPassword(password, user.passwordHash)) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Invalid password"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun userExists(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            userDao.userExists(email.trim().lowercase()) > 0
        }
    }
}
