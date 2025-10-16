package com.test.tadia

import android.app.Application
import com.test.tadia.data.AppDatabase
import com.test.tadia.data.ReservationRepository
import com.test.tadia.data.createReservation
import com.test.tadia.data.RecurringPattern
import com.test.tadia.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class TadIAApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database) }
    val reservationRepository by lazy { ReservationRepository(database.reservationDao()) }
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        initializeSampleData()
    }
    
    private fun initializeSampleData() {
        applicationScope.launch {
            // Check if we already have reservations
            val existingReservations = reservationRepository.getAllReservations()
            if (existingReservations.isEmpty()) {
                val today = LocalDate.now()
                
                // Add sample reservations
                reservationRepository.addReservation(
                    createReservation(
                        id = "1",
                        roomId = "1",
                        roomName = "Sala de Cine",
                        userName = "Daniela Mesa",
                        userEmail = "daniela.mesa@university.edu",
                        date = today,
                        startTime = LocalTime.of(7, 0),
                        endTime = LocalTime.of(9, 0),
                        purpose = "Presentación de proyecto",
                        isRecurring = false,
                        createdByEmail = "daniela.mesa@university.edu"
                    )
                )
                
                reservationRepository.addReservation(
                    createReservation(
                        id = "2",
                        roomId = "1", 
                        roomName = "Sala de Cine",
                        userName = "Juan Jose Rodriguez",
                        userEmail = "juan.rodriguez@university.edu",
                        date = today,
                        startTime = LocalTime.of(10, 0),
                        endTime = LocalTime.of(11, 0),
                        purpose = "Reunión de equipo",
                        isRecurring = false,
                        createdByEmail = "juan.rodriguez@university.edu"
                    )
                )
                
                reservationRepository.addReservation(
                    createReservation(
                        id = "3",
                        roomId = "1",
                        roomName = "Sala de Cine", 
                        userName = "Andre Correa Torres",
                        userEmail = "andre.correa@university.edu",
                        date = today,
                        startTime = LocalTime.of(15, 0),
                        endTime = LocalTime.of(18, 0),
                        purpose = "Clase de cine",
                        isRecurring = true,
                        recurringPattern = RecurringPattern.WEEKLY,
                        createdByEmail = "andre.correa@university.edu"
                    )
                )
            }
        }
    }
}
