package com.test.tadia.repository

import com.test.tadia.data.InfoCategory
import com.test.tadia.data.UniversityInfo

/**
 * Extension function to easily add library room information
 * Use this to add rooms from the biblioteca reservation form
 */
fun UniversityInfoRepository.addLibraryRooms(): List<UniversityInfo> {
    return listOf(
        // TODO: Add rooms from https://forms.office.com/r/BuBSsBB6w9
        
        // Example structure - replace with actual data from the form
        UniversityInfo(
            id = "sala-biblioteca-101",
            title = "Sala Biblioteca 101",
            description = "Sala de estudio disponible en la biblioteca. Requiere reserva mediante formulario online.",
            category = InfoCategory.LIBRARY_SERVICES,
            location = "Biblioteca, Piso 1, Sala 101",
            contactEmail = "biblioteca@utadeo.edu.co",
            requirements = listOf(
                "Carnet estudiantil vigente",
                "Reserva mediante formulario online",
                "Máximo 4 horas por sesión"
            ),
            hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
            availability = "Sujeto a disponibilidad",
            additionalInfo = mapOf(
                "capacity" to "4 personas",
                "equipment" to "Mesa, sillas, pizarra",
                "reservation_url" to "https://forms.office.com/r/BuBSsBB6w9",
                "reservation_method" to "Formulario online",
                "max_duration" to "4 horas"
            )
        ),
        
        UniversityInfo(
            id = "sala-biblioteca-201",
            title = "Sala Biblioteca 201",
            description = "Sala de estudio disponible en la biblioteca. Requiere reserva mediante formulario online.",
            category = InfoCategory.LIBRARY_SERVICES,
            location = "Biblioteca, Piso 2, Sala 201",
            contactEmail = "biblioteca@utadeo.edu.co",
            requirements = listOf(
                "Carnet estudiantil vigente",
                "Reserva mediante formulario online",
                "Máximo 4 horas por sesión"
            ),
            hours = "Lunes a Viernes: 7:00 AM - 9:00 PM, Sábados: 8:00 AM - 2:00 PM",
            availability = "Sujeto a disponibilidad",
            additionalInfo = mapOf(
                "capacity" to "6 personas",
                "equipment" to "Mesa grande, sillas, pizarra, proyector",
                "reservation_url" to "https://forms.office.com/r/BuBSsBB6w9",
                "reservation_method" to "Formulario online",
                "type" to "Sala grupal",
                "max_duration" to "4 horas"
            )
        )
        
        // ADD MORE ROOMS HERE BASED ON THE FORM
        // Copy the structure above and modify for each room
    )
}

