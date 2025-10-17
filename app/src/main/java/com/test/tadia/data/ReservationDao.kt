package com.test.tadia.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations WHERE roomId = :roomId AND date = :date")
    suspend fun getReservationsByRoomAndDate(roomId: String, date: String): List<Reservation>

    @Query("SELECT * FROM reservations WHERE roomId = :roomId")
    suspend fun getReservationsByRoom(roomId: String): List<Reservation>

    @Query("SELECT * FROM reservations WHERE id = :reservationId")
    suspend fun getReservationById(reservationId: String): Reservation?

    @Query("SELECT * FROM reservations")
    suspend fun getAllReservations(): List<Reservation>

    @Query("SELECT * FROM reservations")
    fun getAllReservationsFlow(): Flow<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE roomId = :roomId")
    fun getReservationsByRoomFlow(roomId: String): Flow<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE roomId = :roomId AND date = :date")
    fun getReservationsByRoomAndDateFlow(roomId: String, date: String): Flow<List<Reservation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation)

    @Update
    suspend fun updateReservation(reservation: Reservation)

    @Delete
    suspend fun deleteReservation(reservation: Reservation)

    @Query("DELETE FROM reservations WHERE id = :reservationId")
    suspend fun deleteReservationById(reservationId: String)

    @Query("DELETE FROM reservations")
    suspend fun deleteAllReservations()
}
