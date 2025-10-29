package com.test.tadia.data

/**
 * Data class to represent university information (spaces, rules, contacts, etc.)
 * This information is used by the TadIA chatbot to answer user queries
 */
data class UniversityInfo(
    val id: String,
    val title: String,
    val description: String,
    val category: InfoCategory,
    val location: String? = null,
    val contactEmail: String? = null,
    val contactPhone: String? = null,
    val requirements: List<String> = listOf(),
    val hours: String? = null,
    val availability: String? = null,
    val additionalInfo: Map<String, String> = mapOf()
)

enum class InfoCategory {
    ROOM_RESERVATION,  // Information about reserving rooms
    EQUIPMENT_RENTAL,  // Information about borrowing equipment
    LIBRARY_SERVICES,  // Library information and services
    STUDENT_SERVICES,  // General student services
    CAMPUS_FACILITIES,  // Campus facilities information
    GENERAL_INFO       // General university information
}

