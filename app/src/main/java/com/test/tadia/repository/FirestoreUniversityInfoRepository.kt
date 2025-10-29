package com.test.tadia.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.test.tadia.data.InfoCategory
import com.test.tadia.data.UniversityInfo
import kotlinx.coroutines.tasks.await

/**
 * Firestore-based implementation of UniversityInfoRepository
 * This loads university information from Firestore database
 * 
 * Collection structure in Firestore:
 *   /university-info/{id}
 *     - title: String
 *     - description: String
 *     - category: String (enum name)
 *     - location: String
 *     - contactEmail: String
 *     - contactPhone: String
 *     - requirements: Array<String>
 *     - hours: String
 *     - additionalInfo: Map<String, String>
 */
class FirestoreUniversityInfoRepository {
    
    private val db = FirebaseFirestore.getInstance()
    private val COLLECTION = "university-info"
    
    /**
     * Get all university information from Firestore
     */
    suspend fun getAllUniversityInfo(): List<UniversityInfo> {
        return try {
            val snapshot = db.collection(COLLECTION).get().await()
            
            snapshot.documents.mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null
                    
                    UniversityInfo(
                        id = doc.id,
                        title = data["title"] as? String ?: "",
                        description = data["description"] as? String ?: "",
                        category = try {
                            InfoCategory.valueOf(data["category"] as? String ?: "GENERAL_INFO")
                        } catch (e: Exception) {
                            InfoCategory.GENERAL_INFO
                        },
                        location = data["location"] as? String,
                        contactEmail = data["contactEmail"] as? String,
                        contactPhone = data["contactPhone"] as? String,
                        requirements = (data["requirements"] as? List<*>)?.mapNotNull { 
                            it as? String 
                        } ?: listOf(),
                        hours = data["hours"] as? String,
                        availability = data["availability"] as? String,
                        additionalInfo = (data["additionalInfo"] as? Map<*, *>)?.mapNotNull { 
                            entry -> entry.key.toString() to (entry.value as? String ?: "")
                        }?.toMap() ?: mapOf()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
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
    
    /**
     * Add or update university information (Admin function)
     */
    suspend fun addUniversityInfo(info: UniversityInfo) {
        try {
            val data = hashMapOf(
                "title" to info.title,
                "description" to info.description,
                "category" to info.category.name,
                "location" to (info.location ?: ""),
                "contactEmail" to (info.contactEmail ?: ""),
                "contactPhone" to (info.contactPhone ?: ""),
                "requirements" to info.requirements,
                "hours" to (info.hours ?: ""),
                "availability" to (info.availability ?: ""),
                "additionalInfo" to info.additionalInfo
            )
            
            db.collection(COLLECTION).document(info.id).set(data).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al guardar información: ${e.message}")
        }
    }
    
    /**
     * Delete university information (Admin function)
     */
    suspend fun deleteUniversityInfo(id: String) {
        try {
            db.collection(COLLECTION).document(id).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error al eliminar información: ${e.message}")
        }
    }
}

