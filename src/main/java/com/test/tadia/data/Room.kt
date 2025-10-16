package com.test.tadia.data

data class Room(
    val id: String,
    val name: String,
    val description: String,
    val capacity: Int,
    val equipment: List<String> = emptyList(),
    val isAvailable: Boolean = true
)

// Sample rooms data
object RoomRepository {
    val rooms = listOf(
        Room(
            id = "1",
            name = "Sala de Cine",
            description = "Sala equipada con proyector y sistema de sonido",
            capacity = 50,
            equipment = listOf("Proyector", "Sistema de sonido", "Pantalla", "Sillas")
        ),
        Room(
            id = "2", 
            name = "Sala de Música",
            description = "Sala con instrumentos musicales y equipos de grabación",
            capacity = 20,
            equipment = listOf("Piano", "Guitarras", "Micrófonos", "Consola de mezcla")
        ),
        Room(
            id = "3",
            name = "Sala de Salsa",
            description = "Sala de baile con espejos y sistema de sonido",
            capacity = 30,
            equipment = listOf("Espejos", "Sistema de sonido", "Piso de baile", "Iluminación")
        )
    )
}
