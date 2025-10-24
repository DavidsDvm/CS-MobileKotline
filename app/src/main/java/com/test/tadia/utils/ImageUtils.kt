package com.test.tadia.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ImageUploadManager {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    suspend fun uploadImage(uri: Uri, newsId: String): String? {
        return try {
            val imageRef = storageRef.child("news_images/$newsId/${UUID.randomUUID()}.jpg")
            val uploadTask = imageRef.putFile(uri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteImage(imageUrl: String): Boolean {
        return try {
            val imageRef = storage.getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}

@Composable
fun rememberImagePicker(
    onImageSelected: (Uri?) -> Unit
): () -> Unit {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        onImageSelected(uri)
    }

    return {
        launcher.launch("image/*")
    }
}

@Composable
fun rememberImagePickerWithCamera(
    onImageSelected: (Uri?) -> Unit
): Pair<() -> Unit, () -> Unit> {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        onImageSelected(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            onImageSelected(selectedImageUri)
        }
    }

    val openGallery = {
        galleryLauncher.launch("image/*")
    }

    val openCamera = {
        // For camera, you would need to create a temporary file
        // This is a simplified version - in production you'd handle file creation properly
        openGallery() // Fallback to gallery for now
    }

    return Pair(openGallery, openCamera)
}
