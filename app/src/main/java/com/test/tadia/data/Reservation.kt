package com.test.tadia.data

import com.google.firebase.firestore.DocumentId
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

data class Reservation(
    @DocumentId
    val id: String = "",
    val roomId: String = "",
    val roomName: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val date: String = "", // Store as String for Firebase compatibility
    val startTime: String = "", // Store as String for Firebase compatibility
    val endTime: String = "", // Store as String for Firebase compatibility
    val purpose: String = "",
    val isRecurring: Boolean = false,
    val recurringPattern: String? = null, // Store as String for Firebase compatibility
    val status: String = "CONFIRMED", // Store as String for Firebase compatibility
    val createdAt: Date = Date(),
    val createdByEmail: String = "" // Track who created the reservation
)

enum class ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}

enum class RecurringPattern {
    DAILY,
    WEEKLY,
    MONTHLY
}


data class TimeSlot(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isAvailable: Boolean = true,
    val reservation: Reservation? = null
)

// Legacy ReservationRepository - now replaced by database-backed ReservationRepository
// This is kept for backward compatibility but should not be used
@Deprecated("Use ReservationRepository from data package instead")
object LegacyReservationRepository {
    private val reservations = mutableListOf<Reservation>()
    
    fun addReservation(reservation: Reservation) {
        reservations.add(reservation)
    }
    
    fun updateReservation(reservation: Reservation) {
        val index = reservations.indexOfFirst { it.id == reservation.id }
        if (index != -1) {
            reservations[index] = reservation
        }
    }
    
    fun deleteReservation(reservationId: String) {
        reservations.removeAll { it.id == reservationId }
    }
    
    fun getReservationsByRoomAndDate(roomId: String, date: LocalDate): List<Reservation> {
        return reservations.filter { 
            it.roomId == roomId && it.getDate() == date 
        }
    }
    
    fun getReservationById(reservationId: String): Reservation? {
        return reservations.find { it.id == reservationId }
    }
    
    fun getAllReservations(): List<Reservation> = reservations.toList()
    
    // Initialize with sample data
    init {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        
        addReservation(
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
        
        addReservation(
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
        
        addReservation(
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
