package com.test.tadia.service

import com.google.ai.client.generativeai.GenerativeModel
import com.test.tadia.repository.UniversityInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Service to interact with Google's Gemini AI for chatbot responses
 * 
 * To use this service, you need to:
 * 1. Get a Gemini API key from: https://ai.google.dev/
 * 2. Replace the API_KEY constant below with your actual key
 * 3. The service will handle university-specific queries about room reservations, facilities, etc.
 */
class GeminiService {
    
    private val universityInfoRepository = UniversityInfoRepository()
    
    companion object {
        // TODO: Replace with your actual API key from https://ai.google.dev/
        private const val API_KEY = "AIzaSyBmkIC6jC7Lj6-vB7_F0g1cEaoZVsTAtWk"
        
        // System prompt to guide the AI's behavior for university queries
        private const val SYSTEM_PROMPT = """
            Eres TadIA, un asistente virtual de la universidad Tadeo. Tu función es ayudar a los estudiantes 
            con información sobre:
            
            - Agendamiento de espacios y salas
            - Ubicaciones de instalaciones (biblioteca, salas, oficinas)
            - Políticas y procedimientos universitarios
            - Préstamo de equipos (portátiles, etc.)
            - Horarios y disponibilidad
            - Contactos de servicios universitarios
            
            INSTRUCCIONES CRÍTICAS DE FORMATO:
            1. NO uses markdown, asteriscos (* o **) ni símbolos de formato.
            2. USA EMOJIS en lugar de símbolos: 📍 ubicación, 🕐 horarios, 📌 requisitos, ⏱️ duración, 📧 email
            3. Para listas, usa guiones (-) o números (1. 2. 3.)
            4. Para destacar texto importante, usa emojis o mayúsculas
            5. Organiza la información con saltos de línea claros
            
            INSTRUCCIONES DE CONTENIDO Y CONVERSACIÓN:
            1. NO tires toda la información de una vez. Sé CONVERSACIONAL e INTERACTIVO.
            2. Primera respuesta: Da una respuesta BREVE con información básica y pregunta si quiere más detalles.
               Ejemplo: "¡Claro! La Sala Infantil está en la Biblioteca, Tercer Piso. ¿Te gustaría conocer los requisitos, horarios y cómo reservarla?"
            3. SI el usuario dice "sí" o "cuéntame más" o "sí quiero" o "dame más info": ENTONCES da TODOS los detalles
            4. SI el usuario hace preguntas específicas: contesta solo esa pregunta específica de manera concisa
            5. SI el contexto contiene la información: úsala, pero distribúyela en múltiples mensajes
            6. Sé amigable, conversacional y profesional
            7. NO seas repetitivo - no vuelvas a dar info que ya dijiste
            8. Responde en español de manera natural
            9. CRÍTICO: Siempre mantén el contexto del tema del que estabas hablando. Si el usuario pregunta "sí" o "requisitos" o "horarios" sin especificar, se refiere al ÚLTIMO tema que mencionaste en tu mensaje anterior. NO cambies de instalación o tema. Si hablabas de "Cancha Polifuncional", mantén ese contexto. Si hablabas de "Cancha de Squash", mantén ese contexto.
            10. Si mencionaste varias instalaciones, el usuario se refiere a la ÚLTIMA que mencionaste en tu respuesta.
        """
    }
    
    private val model: GenerativeModel? by lazy {
        try {
            GenerativeModel(
                modelName = "gemini-2.5-flash",
                apiKey = API_KEY
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun getChatResponse(
        userMessage: String,
        conversationHistory: List<Pair<String, String>> = emptyList()
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                val currentModel = model
                if (currentModel == null) {
                    return@withContext "Servicio de IA no disponible. Verifica tu configuración."
                }
                
                // Extract key terms from conversation history to improve search
                val allUserMessages = conversationHistory.map { it.first } + userMessage
                val combinedQuery = allUserMessages.joinToString(" ")
                
                // Get relevant university information for context
                val context = universityInfoRepository.getContextForAI(combinedQuery)
                
                // Build conversation history context
                val historyContext = if (conversationHistory.isNotEmpty()) {
                    val historyText = conversationHistory.takeLast(6).joinToString("\n") { (userMsg, botMsg) ->
                        "USUARIO: $userMsg\nTADIA: $botMsg"
                    }
                    "\nHISTORIAL DE LA CONVERSACIÓN (últimos mensajes):\n$historyText\n"
                } else {
                    ""
                }
                
                // Create the prompt with context
                val prompt = """
$SYSTEM_PROMPT

$historyContext

CONTEXTO DE LA UNIVERSIDAD:
$context

IMPORTANTE - MANTENER CONTEXTO:
- Si el usuario pregunta "sí", "requisitos", "horarios", "cuéntame más" sin especificar qué instalación: se refiere a la ÚLTIMA instalación o servicio que MENCIONASTE en tu mensaje anterior.
- Busca en el historial cuál fue el último tema del que estabas hablando y mantén ese contexto exacto.
- Ejemplo: Si dijiste "Cancha Polifuncional está en...", y luego preguntan "requisitos", responde sobre Cancha Polifuncional, NO sobre otra instalación.
- NUNCA confundas instalaciones diferentes (Cancha Polifuncional ≠ Cancha de Squash).
- Si mencionaste varias instalaciones, el usuario se refiere a la ÚLTIMA que mencionaste.

USUARIO: $userMessage

TADIA (responde en español de manera clara y útil, manteniendo el contexto de lo que se estaba discutiendo):
""".trimIndent()
                
                val response = currentModel.generateContent(prompt)
                
                // Extract and return the response text
                response.text ?: "No pude generar una respuesta. Por favor, intenta reformular tu pregunta."
            } catch (e: Exception) {
                e.printStackTrace()
                throw Exception("Error al comunicarse con Gemini: ${e.message}")
            }
        }
    }
}

