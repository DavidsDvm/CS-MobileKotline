package com.test.tadia.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {
    /**
     * Hash a password using BCrypt
     */
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    /**
     * Verify a password against its hash
     */
    fun verifyPassword(password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }

    /**
     * Check if password meets minimum requirements
     */
    fun isPasswordValid(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                (password.any { it.isDigit() } || password.any { "!@#\$%^&*()_-+=[]{}|:;,.?/~`".contains(it) })
    }
}
