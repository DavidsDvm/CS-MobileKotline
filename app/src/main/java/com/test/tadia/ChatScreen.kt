package com.test.tadia

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.tadia.data.ChatHistory
import com.test.tadia.data.ChatMessage
import com.test.tadia.viewmodel.ChatHistoryViewModel
import com.test.tadia.viewmodel.ChatViewModel
import com.test.tadia.viewmodel.ChatUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    currentUserEmail: String,
    onBack: () -> Unit,
    chatId: String? = null,
    viewModel: ChatViewModel = viewModel(),
    historyViewModel: ChatHistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val historyState by historyViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    
    // Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Load chats when screen opens
    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail.isNotBlank()) {
            println("ChatScreen: Loading chats for user: $currentUserEmail")
            historyViewModel.loadChats(currentUserEmail)
        }
    }
    
    // Refresh chats when drawer opens
    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.isOpen && currentUserEmail.isNotBlank()) {
            println("ChatScreen: Drawer opened, refreshing chats")
            historyViewModel.loadChats(currentUserEmail)
        }
    }
    
    // Load chat if chatId is provided
    LaunchedEffect(chatId) {
        chatId?.let { id ->
            viewModel.loadChat(id)
        }
    }
    
    // Auto-scroll to bottom when new message arrives
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            delay(100)
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatHistoryDrawer(
                chats = historyState.chats,
                isLoading = historyState.isLoading,
                errorMessage = historyState.errorMessage,
                onChatSelected = { selectedChatId ->
                    println("ChatScreen: Chat selected: $selectedChatId")
                    viewModel.loadChat(selectedChatId)
                    scope.launch {
                        drawerState.close()
                    }
                },
                onNewChat = {
                    scope.launch {
                        // Save current chat before starting new one
                        viewModel.startNewChat(currentUserEmail)
                        // Wait a bit longer to ensure Firestore has time to process
                        kotlinx.coroutines.delay(800)
                        // Refresh history after saving and starting new chat
                        historyViewModel.loadChats(currentUserEmail)
                        drawerState.close()
                    }
                },
                onDeleteChat = { chatIdToDelete ->
                    scope.launch {
                        // Eliminar el historial gente
                        historyViewModel.deleteChat(chatIdToDelete)
                        // Esto fixea los chats si estoy en el chat, si estoy en el chat que
                        // elimine se reestablece la interfaz de inicio - Giovanny
                        if (viewModel.uiState.value.chatHistoryId == chatIdToDelete) {
                            viewModel.startNewChat(currentUserEmail)
                        }

                        historyViewModel.loadChats(currentUserEmail)
                        drawerState.close()
                    }
                },
                onRefresh = {
                    println("ChatScreen: Manual refresh triggered")
                    historyViewModel.loadChats(currentUserEmail)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header with menu button
            ChatHeader(
                onBack = onBack,
                onMenuClick = { 
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
            
            // Chat content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (uiState.messages.isEmpty()) {
                    // Initial state with large dog avatar
                    ChatEmptyState()
                } else {
                    // Chat messages
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(count = uiState.messages.size) { index ->
                            ChatBubble(uiState.messages[index])
                        }
                    }
                }
            }
            
            // Input area
            ChatInputBar(
                onSendMessage = { message ->
                    viewModel.sendMessage(message, currentUserEmail)
                },
                isLoading = uiState.isLoading
            )
        }
    }
}

@Composable
private fun ChatHeader(
    onBack: () -> Unit,
    onMenuClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Menu button (hamburger menu) to open drawer
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Historial de chats",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // TadIA icon/avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                // You can add an avatar here if you have one
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = "TadIA",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ChatEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large dog avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            // Simple emoji or text instead of trying to load a complex drawable
            Text(
                text = "🐕",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "TadIA",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Hola, ¿en qué puedo ayudarte?",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    // Debug log
    LaunchedEffect(message.id) {
        println("ChatBubble: Rendering message - isUser=${message.isUser}, text=${message.text.take(30)}")
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .align(if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart)
                .widthIn(max = (280 + 50).dp),  // Account for avatar space
            verticalAlignment = Alignment.Bottom
        ) {
            if (!message.isUser) {
                // Bot avatar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🐕",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            // Message bubble
            Surface(
                modifier = Modifier.widthIn(max = 280.dp),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isUser) 16.dp else 4.dp,
                    bottomEnd = if (message.isUser) 4.dp else 16.dp
                ),
                color = if (message.isUser) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = if (message.isUser) 0.dp else 1.dp
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = if (message.isUser) FontWeight.Normal else FontWeight.Normal
                    ),
                    color = if (message.isUser) 
                        Color.White 
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
            }
            
            if (message.isUser) {
                // User avatar
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatInputBar(
    onSendMessage: (String) -> Unit,
    isLoading: Boolean
) {
    var message by remember { mutableStateOf("") }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Mensaje...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(24.dp),
                enabled = !isLoading,
                singleLine = false,
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(
                onClick = {
                    if (message.isNotBlank() && !isLoading) {
                        onSendMessage(message)
                        message = ""
                    }
                },
                enabled = !isLoading && message.isNotBlank(),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = if (isLoading || message.isBlank()) 
                        MaterialTheme.colorScheme.onSurfaceVariant 
                    else 
                        MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ChatHistoryDrawer(
    chats: List<ChatHistory>,
    isLoading: Boolean,
    onChatSelected: (String) -> Unit,
    onNewChat: () -> Unit,
    onDeleteChat: (String) -> Unit,
    onRefresh: () -> Unit,
    errorMessage: String? = null
) {
    var chatToDelete by remember { mutableStateOf<String?>(null) }
    
    // Debug logging
    LaunchedEffect(chats.size) {
        println("ChatHistoryDrawer: chats.size = ${chats.size}, isLoading = $isLoading")
    }
    
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chats",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        IconButton(onClick = onNewChat) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Nueva conversación",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
            
            // New Chat Button
            Button(
                onClick = onNewChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nueva conversación")
            }
            
            Divider()
            
            // Error message
            errorMessage?.let { error ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Error: $error",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onRefresh) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            
            // Chat list
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (chats.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "No hay conversaciones",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onRefresh) {
                            Text("Actualizar")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = chats,
                        key = { it.id }
                    ) { chat ->
                        ChatDrawerItem(
                            chat = chat,
                            onChatClick = { onChatSelected(chat.id) },
                            onDeleteClick = { chatToDelete = chat.id }
                        )
                    }
                }
            }
        }
    }
    
    // Delete confirmation dialog
    if (chatToDelete != null) {
        AlertDialog(
            onDismissRequest = { chatToDelete = null },
            title = { Text("Eliminar conversación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta conversación?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        chatToDelete?.let { onDeleteChat(it) }
                        chatToDelete = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { chatToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ChatDrawerItem(
    chat: ChatHistory,
    onChatClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onChatClick),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chat.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1
                )
                
                if (chat.lastMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = chat.lastMessage,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDate(chat.lastMessageTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    
    Divider()
}

private fun formatDate(date: Date): String {
    val now = Date()
    val diff = now.time - date.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    
    return when {
        seconds < 60 -> "Hace un momento"
        minutes < 60 -> "Hace $minutes min"
        hours < 24 -> "Hace $hours h"
        days == 1L -> "Ayer"
        days < 7 -> "Hace $days días"
        else -> {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(date)
        }
    }
}

