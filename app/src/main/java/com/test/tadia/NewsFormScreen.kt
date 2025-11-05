package com.test.tadia

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.test.tadia.utils.ImageUploadManager
import com.test.tadia.utils.rememberImagePicker
import com.test.tadia.viewmodel.ImageUploadViewModel
import android.net.Uri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.tadia.data.News
import com.test.tadia.data.NewsType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFormScreen(
    news: News? = null,
    isLoading: Boolean,
    errorMessage: String?,
    onSaveNews: (News) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(news?.title ?: "") }
    var description by remember { mutableStateOf(news?.description ?: "") }
    var summary by remember { mutableStateOf(news?.summary ?: "") }
    var selectedType by remember { mutableStateOf(news?.type ?: NewsType.SIMPLE) }
    var keywords by remember { mutableStateOf(news?.keywords?.joinToString(", ") ?: "") }
    var isPublished by remember { mutableStateOf(news?.isPublished ?: true) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf(news?.imageUrl ?: "") }
    var isUploadingImage by remember { mutableStateOf(false) }

    val isEditing = news != null
    val context = LocalContext.current
    val imageUploadViewModel: ImageUploadViewModel = viewModel()
    val imageUploadState = imageUploadViewModel.uiState

    // Image picker
    val pickImage = rememberImagePicker { uri ->
        selectedImageUri = uri
        if (uri != null) {
            // Upload image when selected
            val newsId = news?.id ?: System.currentTimeMillis().toString()
            imageUploadViewModel.uploadImage(uri, newsId)
        }
    }

    // Update uploaded image URL when upload completes
    LaunchedEffect(imageUploadState.uploadedImageUrl) {
        imageUploadState.uploadedImageUrl?.let { url ->
            uploadedImageUrl = url
        }
    }

    // Update uploading state
    LaunchedEffect(imageUploadState.isUploading) {
        isUploadingImage = imageUploadState.isUploading
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text(
                    "NOTICIAS",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // News Type Selection
            Text(
                text = "Tipo de noticia",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .selectable(
                            selected = selectedType == NewsType.SIMPLE,
                            onClick = { selectedType = NewsType.SIMPLE }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedType == NewsType.SIMPLE,
                        onClick = { selectedType = NewsType.SIMPLE }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Noticia simple")
                }
                
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .selectable(
                            selected = selectedType == NewsType.COMPLEX,
                            onClick = { selectedType = NewsType.COMPLEX }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedType == NewsType.COMPLEX,
                        onClick = { selectedType = NewsType.COMPLEX }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Noticia compleja")
                }
            }

            Spacer(Modifier.height(24.dp))

            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título *") },
                placeholder = { Text("Título...") },
                modifier = Modifier.fillMaxWidth(),
                isError = title.isBlank()
            )

            Spacer(Modifier.height(16.dp))

            // Summary Field (only for complex news)
            if (selectedType == NewsType.COMPLEX) {
                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("Resumen") },
                    placeholder = { Text("Resumen...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
                Spacer(Modifier.height(16.dp))
            }

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción *") },
                placeholder = { Text("Descripción...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 8,
                isError = description.isBlank()
            )

            Spacer(Modifier.height(16.dp))

            // Keywords Field
            OutlinedTextField(
                value = keywords,
                onValueChange = { keywords = it },
                label = { Text("Palabras clave") },
                placeholder = { Text("Separadas por comas...") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Ejemplo: Música, Emprendimientos, Libros") }
            )

            Spacer(Modifier.height(16.dp))

            // Image Upload Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { pickImage() },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedImageUri != null || uploadedImageUrl.isNotEmpty()) {
                        // Show selected/uploaded image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            if (selectedImageUri != null) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Selected image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (uploadedImageUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(uploadedImageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "News image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            
                            // Upload progress overlay
                            if (isUploadingImage) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Toca para cambiar la imagen",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        // Show placeholder
                        Icon(
                            Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Cargar imagen",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Toca para seleccionar una imagen",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Published Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Publicar inmediatamente",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = isPublished,
                    onCheckedChange = { isPublished = it }
                )
            }

            Spacer(Modifier.height(32.dp))

            // Error Message
            if (errorMessage != null || imageUploadState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = errorMessage ?: imageUploadState.errorMessage ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Save Button
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        val newsKeywords = if (keywords.isNotBlank()) {
                            keywords.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        } else {
                            emptyList()
                        }

                        val newsToSave = if (isEditing) {
                            news!!.copy(
                                title = title,
                                description = description,
                                summary = if (selectedType == NewsType.COMPLEX) summary else null,
                                type = selectedType,
                                keywords = newsKeywords,
                                imageUrl = uploadedImageUrl,
                                isPublished = isPublished
                            )
                        } else {
                            com.test.tadia.data.createNews(
                                title = title,
                                description = description,
                                summary = if (selectedType == NewsType.COMPLEX) summary else null,
                                type = selectedType,
                                keywords = newsKeywords,
                                imageUrl = uploadedImageUrl,
                                authorName = "Usuario", // This should come from current user
                                authorEmail = "user@test.com", // This should come from current user
                                isPublished = isPublished
                            )
                        }
                        onSaveNews(newsToSave)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && title.isNotBlank() && description.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (isEditing) "Confirmar cambios" else "Crear noticia",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsFormScreenPreview() {
    MaterialTheme {
        NewsFormScreen(
            news = null,
            isLoading = false,
            errorMessage = null,
            onSaveNews = {},
            onBack = {}
        )
    }
}
