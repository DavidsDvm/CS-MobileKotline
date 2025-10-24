package com.test.tadia

import android.app.Application
import com.test.tadia.data.Reservation
import com.test.tadia.data.RecurringPattern
import com.test.tadia.data.News
import com.test.tadia.data.NewsType
import com.test.tadia.repository.FirebaseUserRepository
import com.test.tadia.repository.FirebaseReservationRepository
import com.test.tadia.repository.FirebaseNewsRepository
import com.test.tadia.data.createReservation
import com.test.tadia.data.createNews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TadIAApplication : Application() {
    val userRepository by lazy { FirebaseUserRepository() }
    val reservationRepository by lazy { FirebaseReservationRepository() }
    val newsRepository by lazy { FirebaseNewsRepository() }
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onCreate() {
        super.onCreate()
        initializeSampleData()
        initializeSampleNews()
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
                        id = UUID.randomUUID().toString(),
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
                        id = UUID.randomUUID().toString(),
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
                        id = UUID.randomUUID().toString(),
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
    
    private fun initializeSampleNews() {
        applicationScope.launch {
            // Check if we already have news
            val existingNews = newsRepository.getAllNews()
            if (existingNews.isEmpty()) {
                // Add sample news
                newsRepository.addNews(
                    createNews(
                        id = "1",
                        title = "E Sports Day",
                        description = "Gran evento de deportes electrónicos con múltiples actividades en torno a la cultura, el entretenimiento y la innovación. El evento incluye torneos de videojuegos, stands de emprendimientos, presentaciones musicales y mucho más.",
                        summary = "Evento anual de deportes electrónicos con actividades culturales y de entretenimiento.",
                        type = NewsType.COMPLEX,
                        keywords = listOf("Música", "Emprendimientos", "Libros", "Juegos De Mesa", "Video Juegos"),
                        imageUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&h=400&fit=crop",
                        authorName = "Admin",
                        authorEmail = "admin@test.com",
                        isPublished = true
                    )
                )
                
                newsRepository.addNews(
                    createNews(
                        id = "2",
                        title = "Museo del mar",
                        description = "El museo del mar ha reabierto sus puertas con nuevas exhibiciones y actividades interactivas para toda la familia.",
                        type = NewsType.SIMPLE,
                        keywords = listOf("Museo", "Cultura", "Familia"),
                        imageUrl = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=800&h=400&fit=crop",
                        authorName = "Admin",
                        authorEmail = "admin@test.com",
                        isPublished = true
                    )
                )
                
                newsRepository.addNews(
                    createNews(
                        id = "3",
                        title = "Biblioteca",
                        description = "Nuevos servicios y horarios extendidos en la biblioteca principal del campus.",
                        type = NewsType.SIMPLE,
                        keywords = listOf("Biblioteca", "Servicios", "Estudio"),
                        imageUrl = "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=800&h=400&fit=crop",
                        authorName = "Admin",
                        authorEmail = "admin@test.com",
                        isPublished = true
                    )
                )
            }
        }
    }
}
