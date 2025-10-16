package com.test.tadia.data

import java.time.LocalDate
import java.time.LocalTime

data class Reservation(
    val id: String,
    val roomId: String,
    val roomName: String,
    val userName: String,
    val userEmail: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val purpose: String,
    val isRecurring: Boolean = false,
    val recurringPattern: RecurringPattern? = null,
    val status: ReservationStatus = ReservationStatus.CONFIRMED,
    val createdAt: Long = System.currentTimeMillis()
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

// Sample reservations data
object ReservationRepository {
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
            it.roomId == roomId && it.date == date 
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
            Reservation(
                id = "1",
                roomId = "1",
                roomName = "Sala de Cine",
                userName = "Daniela Mesa",
                userEmail = "daniela.mesa@university.edu",
                date = today,
                startTime = LocalTime.of(7, 0),
                endTime = LocalTime.of(9, 0),
                purpose = "Presentación de proyecto",
                isRecurring = false
            )
        )
        
        addReservation(
            Reservation(
                id = "2",
                roomId = "1", 
                roomName = "Sala de Cine",
                userName = "Juan Jose Rodriguez",
                userEmail = "juan.rodriguez@university.edu",
                date = today,
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(11, 0),
                purpose = "Reunión de equipo",
                isRecurring = false
            )
        )
        
        addReservation(
            Reservation(
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
                recurringPattern = RecurringPattern.WEEKLY
            )
        )
    }
}
