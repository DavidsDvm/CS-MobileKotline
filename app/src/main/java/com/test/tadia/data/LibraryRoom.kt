package com.test.tadia.data

/**
 * Data class for Library Room information
 * Based on JTL library room reservation system
 */
data class LibraryRoom(
    val id: String,
    val name: String,
    val type: RoomType,
    val location: String,
    val capacity: Int,
    val description: String,
    val equipment: List<String> = listOf(),
    val restrictions: List<String> = listOf(),
    val reservationMethod: ReservationMethod,
    val contactEmail: String? = null,
    val contactPhone: String? = null,
    val bookingHours: String? = null,
    val availability: String? = null,
    val additionalInfo: Map<String, String> = mapOf()
)

enum class RoomType {
    STUDY_ROOM,        // Sala de estudio
    GROUP_ROOM,        // Sala grupal
    CONFERENCE_ROOM,   // Sala de conferencias
    WORKSHOP_ROOM,     // Taller
    AUDIOVISUAL,       // Sala audiovisual
    COMPUTER_LAB,      // Laboratorio de computadores
    INDIVIDUAL_BOOTH   // Cubículo individual
}

enum class ReservationMethod {
    ONLINE_FORM,       // Reservar online en forms.office.com
    EMAIL,            // Enviar correo
    IN_PERSON,        // Reservar en persona
    PHONE,            // Reservar por teléfono
    APP               // Reservar por aplicación
}

