package com.test.tadia.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [User::class, Reservation::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS reservations (
                        id TEXT NOT NULL PRIMARY KEY,
                        roomId TEXT NOT NULL,
                        roomName TEXT NOT NULL,
                        userName TEXT NOT NULL,
                        userEmail TEXT NOT NULL,
                        date TEXT NOT NULL,
                        startTime TEXT NOT NULL,
                        endTime TEXT NOT NULL,
                        purpose TEXT NOT NULL,
                        isRecurring INTEGER NOT NULL DEFAULT 0,
                        recurringPattern TEXT,
                        status TEXT NOT NULL DEFAULT 'CONFIRMED',
                        createdAt INTEGER NOT NULL DEFAULT 0
                    )
                """)
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE reservations ADD COLUMN createdByEmail TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tadia_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
