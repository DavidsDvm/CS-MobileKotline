package com.test.tadia.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ReservationRepository(private val reservationDao: ReservationDao) {
    
    suspend fun addReservation(reservation: Reservation) {
        reservationDao.insertReservation(reservation)
    }
    
    suspend fun updateReservation(reservation: Reservation) {
        reservationDao.updateReservation(reservation)
    }
    
    suspend fun deleteReservation(reservationId: String) {
        reservationDao.deleteReservationById(reservationId)
    }
    
    suspend fun getReservationsByRoomAndDate(roomId: String, date: LocalDate): List<Reservation> {
        return reservationDao.getReservationsByRoomAndDate(roomId, date.toDatabaseString())
    }
    
    fun getReservationsByRoomAndDateFlow(roomId: String, date: LocalDate): Flow<List<Reservation>> {
        return reservationDao.getReservationsByRoomAndDateFlow(roomId, date.toDatabaseString())
    }
    
    suspend fun getReservationById(reservationId: String): Reservation? {
        return reservationDao.getReservationById(reservationId)
    }
    
    suspend fun getAllReservations(): List<Reservation> {
        return reservationDao.getAllReservations()
    }
    
    fun getAllReservationsFlow(): Flow<List<Reservation>> {
        return reservationDao.getAllReservationsFlow()
    }
    
    fun getReservationsByRoomFlow(roomId: String): Flow<List<Reservation>> {
        return reservationDao.getReservationsByRoomFlow(roomId)
    }
    
    suspend fun getReservationsByRoom(roomId: String): List<Reservation> {
        return reservationDao.getReservationsByRoom(roomId)
    }
    
    suspend fun deleteAllReservations() {
        reservationDao.deleteAllReservations()
    }
}
