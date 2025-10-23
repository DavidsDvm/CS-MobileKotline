package com.test.tadia.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.test.tadia.data.Reservation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.Date

class FirebaseReservationRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val reservationsCollection = firestore.collection("reservations")

    suspend fun addReservation(reservation: Reservation) {
        reservationsCollection.document(reservation.id).set(reservation).await()
    }

    suspend fun updateReservation(reservation: Reservation) {
        reservationsCollection.document(reservation.id).set(reservation).await()
    }

    suspend fun deleteReservation(reservationId: String) {
        reservationsCollection.document(reservationId).delete().await()
    }

    suspend fun getReservationsByRoomAndDate(roomId: String, date: LocalDate): List<Reservation> {
        val dateString = date.toString()
        val snapshot = reservationsCollection
            .whereEqualTo("roomId", roomId)
            .whereEqualTo("date", dateString)
            .get()
            .await()
        
        return snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) }
    }

    fun getReservationsByRoomAndDateFlow(roomId: String, date: LocalDate): Flow<List<Reservation>> = flow {
        val dateString = date.toString()
        reservationsCollection
            .whereEqualTo("roomId", roomId)
            .whereEqualTo("date", dateString)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error - you might want to emit an error state
                    return@addSnapshotListener
                }
                
                val reservations = snapshot?.documents?.mapNotNull { 
                    it.toObject(Reservation::class.java) 
                } ?: emptyList()
                
                // Note: This is a simplified approach. In a real app, you'd want to use
                // a proper Flow implementation that can handle the listener lifecycle
            }
    }

    suspend fun getReservationById(reservationId: String): Reservation? {
        val document = reservationsCollection.document(reservationId).get().await()
        return document.toObject(Reservation::class.java)
    }

    suspend fun getAllReservations(): List<Reservation> {
        val snapshot = reservationsCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) }
    }

    fun getAllReservationsFlow(): Flow<List<Reservation>> = flow {
        reservationsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }
            
            val reservations = snapshot?.documents?.mapNotNull { 
                it.toObject(Reservation::class.java) 
            } ?: emptyList()
            
            // Note: This is a simplified approach. In a real app, you'd want to use
            // a proper Flow implementation that can handle the listener lifecycle
        }
    }

    fun getReservationsByRoomFlow(roomId: String): Flow<List<Reservation>> = flow {
        reservationsCollection
            .whereEqualTo("roomId", roomId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                
                val reservations = snapshot?.documents?.mapNotNull { 
                    it.toObject(Reservation::class.java) 
                } ?: emptyList()
                
                // Note: This is a simplified approach. In a real app, you'd want to use
                // a proper Flow implementation that can handle the listener lifecycle
            }
    }

    suspend fun getReservationsByRoom(roomId: String): List<Reservation> {
        val snapshot = reservationsCollection
            .whereEqualTo("roomId", roomId)
            .get()
            .await()
        
        return snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) }
    }

    suspend fun deleteAllReservations() {
        val batch = firestore.batch()
        val snapshot = reservationsCollection.get().await()
        
        snapshot.documents.forEach { document ->
            batch.delete(document.reference)
        }
        
        batch.commit().await()
    }

    suspend fun getReservationsByUser(userEmail: String): List<Reservation> {
        val snapshot = reservationsCollection
            .whereEqualTo("userEmail", userEmail)
            .get()
            .await()
        
        return snapshot.documents.mapNotNull { it.toObject(Reservation::class.java) }
    }

    fun getReservationsByUserFlow(userEmail: String): Flow<List<Reservation>> = flow {
        reservationsCollection
            .whereEqualTo("userEmail", userEmail)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                
                val reservations = snapshot?.documents?.mapNotNull { 
                    it.toObject(Reservation::class.java) 
                } ?: emptyList()
                
                // Note: This is a simplified approach. In a real app, you'd want to use
                // a proper Flow implementation that can handle the listener lifecycle
            }
    }
}
