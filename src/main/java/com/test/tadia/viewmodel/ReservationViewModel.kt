package com.test.tadia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.tadia.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

data class ReservationUiState(
    val selectedRoom: Room? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedReservation: Reservation? = null,
    val reservations: List<Reservation> = emptyList(),
    val timeSlots: List<TimeSlot> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ReservationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReservationUiState())
    val uiState: StateFlow<ReservationUiState> = _uiState.asStateFlow()

    fun selectRoom(room: Room) {
        _uiState.value = _uiState.value.copy(selectedRoom = room)
        loadReservationsForRoom(room.id)
    }

    fun selectDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        _uiState.value.selectedRoom?.let { room ->
            loadReservationsForRoomAndDate(room.id, date)
        }
    }

    fun selectReservation(reservation: Reservation) {
        _uiState.value = _uiState.value.copy(selectedReservation = reservation)
    }

    fun createReservation(
        roomId: String,
        roomName: String,
        userName: String,
        userEmail: String,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        purpose: String,
        isRecurring: Boolean = false,
        recurringPattern: RecurringPattern? = null
    ) {
        val reservation = Reservation(
            id = System.currentTimeMillis().toString(),
            roomId = roomId,
            roomName = roomName,
            userName = userName,
            userEmail = userEmail,
            date = date,
            startTime = startTime,
            endTime = endTime,
            purpose = purpose,
            isRecurring = isRecurring,
            recurringPattern = recurringPattern
        )
        
        ReservationRepository.addReservation(reservation)
        loadReservationsForRoomAndDate(roomId, date)
    }

    fun updateReservation(reservation: Reservation) {
        ReservationRepository.updateReservation(reservation)
        loadReservationsForRoomAndDate(reservation.roomId, reservation.date)
    }

    fun deleteReservation(reservationId: String) {
        ReservationRepository.deleteReservation(reservationId)
        _uiState.value.selectedRoom?.let { room ->
            loadReservationsForRoomAndDate(room.id, _uiState.value.selectedDate)
        }
        _uiState.value = _uiState.value.copy(selectedReservation = null)
    }

    private fun loadReservationsForRoom(roomId: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val reservations = ReservationRepository.getReservationsByRoomAndDate(roomId, _uiState.value.selectedDate)
        val timeSlots = generateTimeSlots(reservations)
        _uiState.value = _uiState.value.copy(
            reservations = reservations,
            timeSlots = timeSlots,
            isLoading = false
        )
    }

    private fun loadReservationsForRoomAndDate(roomId: String, date: LocalDate) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val reservations = ReservationRepository.getReservationsByRoomAndDate(roomId, date)
        val timeSlots = generateTimeSlots(reservations)
        _uiState.value = _uiState.value.copy(
            reservations = reservations,
            timeSlots = timeSlots,
            isLoading = false
        )
    }

    private fun generateTimeSlots(reservations: List<Reservation>): List<TimeSlot> {
        val timeSlots = mutableListOf<TimeSlot>()
        val startHour = 7
        val endHour = 18
        
        for (hour in startHour until endHour) {
            val startTime = LocalTime.of(hour, 0)
            val endTime = LocalTime.of(hour + 1, 0)
            
            val conflictingReservation = reservations.find { reservation ->
                (startTime.isBefore(reservation.endTime) && endTime.isAfter(reservation.startTime))
            }
            
            timeSlots.add(
                TimeSlot(
                    startTime = startTime,
                    endTime = endTime,
                    isAvailable = conflictingReservation == null,
                    reservation = conflictingReservation
                )
            )
        }
        
        return timeSlots
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedRoom = null,
            selectedReservation = null,
            reservations = emptyList(),
            timeSlots = emptyList()
        )
    }
}
