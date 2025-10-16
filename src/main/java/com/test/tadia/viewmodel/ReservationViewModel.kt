package com.test.tadia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.tadia.data.*
import com.test.tadia.data.getDate
import com.test.tadia.data.getStartTime
import com.test.tadia.data.getEndTime
import com.test.tadia.data.createReservation
import com.test.tadia.data.canUserEdit
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

class ReservationViewModel(
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReservationUiState())
    val uiState: StateFlow<ReservationUiState> = _uiState.asStateFlow()
    
    private var currentUserEmail: String? = null

    fun setCurrentUser(userEmail: String) {
        currentUserEmail = userEmail
    }

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
        viewModelScope.launch {
            // Clear any previous error messages and set loading state
            _uiState.value = _uiState.value.copy(errorMessage = null, isLoading = true)
            
            // Check for time conflicts
            val existingReservations = reservationRepository.getReservationsByRoomAndDate(roomId, date)
            println("DEBUG: Checking conflicts for room $roomId on date $date")
            println("DEBUG: Found ${existingReservations.size} existing reservations")
            println("DEBUG: Trying to create reservation from $startTime to $endTime")
            
            val hasConflict = existingReservations.any { existing ->
                val existingStart = existing.getStartTime()
                val existingEnd = existing.getEndTime()
                
                println("DEBUG: Checking against existing: $existingStart to $existingEnd")
                
                // Check if the new reservation overlaps with existing one
                // Two time ranges overlap if: start1 < end2 AND start2 < end1
                val overlaps = startTime.isBefore(existingEnd) && endTime.isAfter(existingStart)
                
                // Also check for exact same time slots
                val sameTime = startTime == existingStart && endTime == existingEnd
                
                val conflict = overlaps || sameTime
                println("DEBUG: Overlaps: $overlaps, SameTime: $sameTime, Conflict: $conflict")
                
                conflict
            }
            
            println("DEBUG: Final conflict result: $hasConflict")
            
            if (hasConflict) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Este espacio ya está reservado en este horario",
                    isLoading = false
                )
                return@launch
            }
            
            val reservation = com.test.tadia.data.createReservation(
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
                recurringPattern = recurringPattern,
                createdByEmail = currentUserEmail ?: userEmail
            )
            
            reservationRepository.addReservation(reservation)
            loadReservationsForRoomAndDate(roomId, date)
            _uiState.value = _uiState.value.copy(errorMessage = null, isLoading = false)
        }
    }

    fun updateReservation(reservation: Reservation) {
        viewModelScope.launch {
            // Check if user can edit this reservation
            if (!reservation.canUserEdit(currentUserEmail ?: "")) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "No tienes permisos para editar esta reservación"
                )
                return@launch
            }
            
            reservationRepository.updateReservation(reservation)
            loadReservationsForRoomAndDate(reservation.roomId, reservation.getDate())
            _uiState.value = _uiState.value.copy(errorMessage = null)
        }
    }

    fun deleteReservation(reservationId: String) {
        viewModelScope.launch {
            val reservation = reservationRepository.getReservationById(reservationId)
            if (reservation != null && !reservation.canUserEdit(currentUserEmail ?: "")) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "No tienes permisos para eliminar esta reservación"
                )
                return@launch
            }
            
            reservationRepository.deleteReservation(reservationId)
            _uiState.value.selectedRoom?.let { room ->
                loadReservationsForRoomAndDate(room.id, _uiState.value.selectedDate)
            }
            _uiState.value = _uiState.value.copy(selectedReservation = null, errorMessage = null)
        }
    }

    private fun loadReservationsForRoom(roomId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val reservations = reservationRepository.getReservationsByRoomAndDate(roomId, _uiState.value.selectedDate)
            val timeSlots = generateTimeSlots(reservations)
            _uiState.value = _uiState.value.copy(
                reservations = reservations,
                timeSlots = timeSlots,
                isLoading = false
            )
        }
    }

    private fun loadReservationsForRoomAndDate(roomId: String, date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val reservations = reservationRepository.getReservationsByRoomAndDate(roomId, date)
            val timeSlots = generateTimeSlots(reservations)
            _uiState.value = _uiState.value.copy(
                reservations = reservations,
                timeSlots = timeSlots,
                isLoading = false
            )
        }
    }

    private fun generateTimeSlots(reservations: List<Reservation>): List<TimeSlot> {
        val timeSlots = mutableListOf<TimeSlot>()
        val startHour = 7
        val endHour = 18
        
        for (hour in startHour until endHour) {
            val startTime = LocalTime.of(hour, 0)
            val endTime = LocalTime.of(hour + 1, 0)
            
            val conflictingReservation = reservations.find { reservation ->
                val reservationStartTime = reservation.getStartTime()
                val reservationEndTime = reservation.getEndTime()
                (startTime.isBefore(reservationEndTime) && endTime.isAfter(reservationStartTime))
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

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
