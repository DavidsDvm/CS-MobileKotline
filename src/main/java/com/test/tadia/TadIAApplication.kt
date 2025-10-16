package com.test.tadia

import android.app.Application
import com.test.tadia.data.AppDatabase
import com.test.tadia.repository.UserRepository

class TadIAApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database) }
}
