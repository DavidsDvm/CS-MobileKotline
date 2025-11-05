package com.test.tadia.repository

import com.test.tadia.data.UniversityInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Hybrid implementation that combines hardcoded and Firestore data
 * This provides fallback data while allowing dynamic updates
 */
class HybridUniversityInfoRepository {
    
    private val hardcodedRepo = UniversityInfoRepository()
    private val firestoreRepo = FirestoreUniversityInfoRepository()
    
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()
    
    /**
     * Get all university information (from Firestore if available, fallback to hardcoded)
     */
    suspend fun getAllUniversityInfo(): List<UniversityInfo> {
        return try {
            // Try to get from Firestore first
            val firestoreData = firestoreRepo.getAllUniversityInfo()
            
            if (firestoreData.isNotEmpty()) {
                _isLoaded.value = true
                firestoreData
            } else {
                // Fallback to hardcoded data
                _isLoaded.value = false
                hardcodedRepo.getAllUniversityInfo()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to hardcoded data on error
            _isLoaded.value = false
            hardcodedRepo.getAllUniversityInfo()
        }
    }
    
    /**
     * Search for information relevant to a user query
     */
    suspend fun searchRelevantInfo(query: String): List<UniversityInfo> {
        val allInfo = getAllUniversityInfo()
        val queryLower = query.lowercase()
        
        return allInfo.filter { info ->
            info.title.lowercase().contains(queryLower) ||
            info.description.lowercase().contains(queryLower) ||
            info.location?.lowercase()?.contains(queryLower) == true ||
            info.category.name.lowercase().contains(queryLower) ||
            info.additionalInfo.values.any { it.lowercase().contains(queryLower) } ||
            info.requirements.any { it.lowercase().contains(queryLower) }
        }
    }
    
    /**
     * Get information formatted for AI context
     */
    suspend fun getContextForAI(query: String): String {
        val relevantInfo = searchRelevantInfo(query)
        
        if (relevantInfo.isEmpty()) {
            return "No hay información específica disponible para esta consulta."
        }
        
        val contextBuilder = StringBuilder()
        contextBuilder.append("Información relevante de la Universidad:\n\n")
        
        relevantInfo.forEach { info ->
            contextBuilder.append("📋 ${info.title}\n")
            contextBuilder.append("📍 Ubicación: ${info.location ?: "No especificada"}\n")
            contextBuilder.append("📝 ${info.description}\n")
            
            if (info.requirements.isNotEmpty()) {
                contextBuilder.append("📌 Requisitos:\n")
                info.requirements.forEach { req ->
                    contextBuilder.append("- $req\n")
                }
            }
            
            if (info.hours != null) {
                contextBuilder.append("🕐 Horarios: ${info.hours}\n")
            }
            
            if (info.contactEmail != null) {
                contextBuilder.append("📧 Email: ${info.contactEmail}\n")
            }
            
            if (info.contactPhone != null) {
                contextBuilder.append("📞 Teléfono: ${info.contactPhone}\n")
            }
            
            if (info.additionalInfo.isNotEmpty()) {
                contextBuilder.append("ℹ️ Información adicional:\n")
                info.additionalInfo.forEach { (key, value) ->
                    contextBuilder.append("- ${key.replaceFirstChar { it.uppercase() }}: $value\n")
                }
            }
            
            contextBuilder.append("\n")
        }
        
        return contextBuilder.toString()
    }
}

