package com.test.tadia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.google.firebase.FirebaseApp
import com.test.tadia.data.User
import com.test.tadia.data.Room
import com.test.tadia.data.Reservation
import com.test.tadia.data.News
import com.test.tadia.data.getDate
import com.test.tadia.data.getStartTime
import com.test.tadia.data.getEndTime
import com.test.tadia.data.getRecurringPattern
import com.test.tadia.ui.theme.TadIATheme
import com.test.tadia.viewmodel.LoginViewModel
import com.test.tadia.viewmodel.RegisterViewModel
import com.test.tadia.viewmodel.ReservationViewModel
import com.test.tadia.viewmodel.NewsViewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        setContent {
            TadIATheme {
                TadIAApp()
            }
        }
    }
}

@Composable
fun TadIAApp() {
    var currentScreen by remember { mutableStateOf("login") }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var selectedBottomTab by remember { mutableStateOf(BottomTab.Home) }
    var loginVmKey by remember { mutableStateOf(0) }
    
    // Reservation flow state
    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var isUpdatingReservation by remember { mutableStateOf(false) }
    
    // News flow state
    var selectedNews by remember { mutableStateOf<News?>(null) }
    var showNewsDeleteConfirmation by remember { mutableStateOf(false) }
    var isUpdatingNews by remember { mutableStateOf(false) }
    
    val application = LocalContext.current.applicationContext as TadIAApplication
    val userRepository = application.userRepository
    val reservationRepository = application.reservationRepository
    val newsRepository = application.newsRepository
    val reservationViewModel: ReservationViewModel = viewModel { ReservationViewModel(reservationRepository) }
    val newsViewModel: NewsViewModel = viewModel { NewsViewModel(newsRepository) }
    
    // Collect UI state at the top level
    val reservationUiState by reservationViewModel.uiState.collectAsState()
    val newsUiState by newsViewModel.uiState.collectAsState()
    
    // Watch for successful reservation operations
    LaunchedEffect(reservationUiState.operationSuccessful, currentScreen) {
        if (currentScreen == "reservation_form" && reservationUiState.operationSuccessful) {
            println("DEBUG: Operation successful, navigating back to calendar")
            // Clear the success flag
            reservationViewModel.clearSuccessFlag()
            // Navigate back to calendar
            selectedReservation = null
            isUpdatingReservation = false
            currentScreen = "calendar"
        }
    }
    
    // Watch for successful news operations
    LaunchedEffect(newsUiState.operationSuccessful, currentScreen) {
        if (currentScreen == "news_form" && newsUiState.operationSuccessful) {
            println("DEBUG: News operation successful, navigating back to news list")
            // Clear the success flag
            newsViewModel.clearSuccessFlag()
            // Navigate back to news list
            selectedNews = null
            isUpdatingNews = false
            currentScreen = "news_list"
        }
    }
    
    // Load news when entering news list
    LaunchedEffect(currentScreen) {
        if (currentScreen == "news_list") {
            newsViewModel.loadAllNews()
        }
    }
    
    val mostrarBarraInferiorTodasPantallas = currentScreen !in listOf("login", "register")

    Scaffold(
        bottomBar = {
            if (mostrarBarraInferiorTodasPantallas) {
                AppBottomBar(
                    selected = selectedBottomTab,
                    onSelected = { tab ->
                        selectedBottomTab = tab
                        when (tab) {
                            BottomTab.Home -> currentScreen = "home"
                            BottomTab.Calendar -> currentScreen = if (selectedRoom == null) "room_selection" else "calendar"
                            BottomTab.Inbox -> currentScreen = "chat"
                            BottomTab.Favorites -> currentScreen = "news_list"
                            BottomTab.Profile -> currentScreen = "profile"
                        }
                    }
                )
            }
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.padding(padding)) {
            when (currentScreen) {
        "login" -> {
            val loginViewModel: LoginViewModel = viewModel(key = "login_$loginVmKey") {
                LoginViewModel(userRepository)
            }
            
            LoginScreen(
                viewModel = loginViewModel,
                onRegister = {
                    currentScreen = "register"
                },
                onLoginWithOutlook = {
                    // TODO: Implement Outlook login
                    println("Outlook login")
                }
            )
            
            // Handle successful login
            val loginUiState by loginViewModel.uiState.collectAsState()
            LaunchedEffect(loginUiState.isLoginSuccessful) {
                if (loginUiState.isLoginSuccessful) {
                    currentUser = loginUiState.currentUser
                    currentUser?.let { user ->
                        reservationViewModel.setCurrentUser(user.email)
                        newsViewModel.setCurrentUser(user.email)
                    }
                    currentScreen = "home"
                    selectedBottomTab = BottomTab.Home
                }
            }
        }
        "register" -> {
            val registerViewModel: RegisterViewModel = viewModel {
                RegisterViewModel(userRepository)
            }
            
            RegisterScreen(
                viewModel = registerViewModel,
                onBackToLogin = {
                    currentScreen = "login"
                }
            )
            
            // Handle successful registration
            val registerUiState by registerViewModel.uiState.collectAsState()
            
            // Use LaunchedEffect with a key that changes
            LaunchedEffect(registerUiState.isRegistrationSuccessful, registerUiState.isLoading) {
                println("DEBUG: Registration state changed - isSuccessful: ${registerUiState.isRegistrationSuccessful}, isLoading: ${registerUiState.isLoading}")
                if (registerUiState.isRegistrationSuccessful && !registerUiState.isLoading) {
                    println("DEBUG: Registration successful, navigating to login")
                    currentScreen = "login"
                    // Reset the registration state after navigation
                    registerViewModel.resetRegistrationState()
                }
            }
        }
        "home" -> {
            currentUser?.let { user ->
                HomeScreen(
                    user = user,
                    onLogout = {
                        currentUser = null
                        currentScreen = "login"
                    },
                    onNavigateToReservations = {
                        currentScreen = "room_selection"
                        selectedBottomTab = BottomTab.Calendar
                    },
                    onNavigateToNews = {
                        currentScreen = "news_list"
                        selectedBottomTab = BottomTab.Favorites
                    },
                    onNavigateToChat = {
                        currentScreen = "chat"
                        selectedBottomTab = BottomTab.Inbox
                    }
                )
            }
        }
        "room_selection" -> {
            RoomSelectionScreen(
                onRoomSelected = { room ->
                    selectedRoom = room
                    reservationViewModel.selectRoom(room)
                    currentScreen = "calendar"
                    selectedBottomTab = BottomTab.Calendar
                },
                onBack = {
                    currentScreen = "home"
                    selectedBottomTab = BottomTab.Home
                }
            )
        }
        "calendar" -> {
            selectedRoom?.let { room ->
                CalendarScreen(
                    room = room,
                    selectedDate = selectedDate,
                    reservations = reservationUiState.reservations,
                    timeSlots = reservationUiState.timeSlots,
                    onDateSelected = { date ->
                        selectedDate = date
                        reservationViewModel.selectDate(date)
                    },
                    onReservationSelected = { reservation ->
                        selectedReservation = reservation
                        currentScreen = "reservation_details"
                    },
                    onNewReservation = {
                        currentScreen = "reservation_form"
                    },
                    onBack = {
                        currentScreen = "room_selection"
                        selectedBottomTab = BottomTab.Calendar
                    }
                )
            }
        }
        "reservation_details" -> {
            selectedRoom?.let { room ->
                currentUser?.let { user ->
                    ReservationDetailsScreen(
                        room = room,
                        selectedDate = selectedDate,
                        reservations = reservationUiState.reservations,
                        timeSlots = reservationUiState.timeSlots,
                        selectedReservation = selectedReservation,
                        currentUserEmail = user.email,
                        errorMessage = reservationUiState.errorMessage,
                        onDateSelected = { date ->
                            selectedDate = date
                            reservationViewModel.selectDate(date)
                        },
                        onReservationSelected = { reservation ->
                            selectedReservation = reservation
                        },
                        onEditReservation = { reservation ->
                            selectedReservation = reservation
                            reservationViewModel.clearErrorMessage()
                            currentScreen = "reservation_form"
                        },
                        onDeleteReservation = { reservation ->
                            reservationViewModel.deleteReservation(reservation.id)
                            selectedReservation = null
                        },
                        onShowDeleteConfirmation = { reservation ->
                            selectedReservation = reservation
                            showDeleteConfirmation = true
                        },
                    onNewReservation = {
                        selectedReservation = null
                        reservationViewModel.clearErrorMessage()
                        currentScreen = "reservation_form"
                    },
                    onBack = {
                        selectedReservation = null
                        reservationViewModel.clearErrorMessage()
                        currentScreen = "calendar"
                        selectedBottomTab = BottomTab.Calendar
                    }
                    )
                }
            }
        }
        "reservation_form" -> {
            selectedRoom?.let { room ->
                ReservationFormScreen(
                    room = room,
                    selectedDate = selectedDate,
                    reservation = selectedReservation,
                    errorMessage = reservationUiState.errorMessage,
                    isLoading = reservationUiState.isLoading,
                    onSaveReservation = { reservation ->
                        if (selectedReservation == null) {
                            // Creating new reservation
                            reservationViewModel.createReservation(
                                roomId = reservation.roomId,
                                roomName = reservation.roomName,
                                userName = reservation.userName,
                                userEmail = reservation.userEmail,
                                date = reservation.getDate(),
                                startTime = reservation.getStartTime(),
                                endTime = reservation.getEndTime(),
                                purpose = reservation.purpose,
                                isRecurring = reservation.isRecurring,
                                recurringPattern = reservation.getRecurringPattern()
                            )
                            // Don't navigate immediately - wait for validation result
                        } else {
                            // Updating existing reservation
                            isUpdatingReservation = true
                            reservationViewModel.updateReservation(reservation)
                            // Navigation will be handled by LaunchedEffect when update completes
                        }
                    },
                    onBack = {
                        reservationViewModel.clearErrorMessage()
                        isUpdatingReservation = false
                        currentScreen = if (selectedReservation == null) "calendar" else "reservation_details"
                        selectedBottomTab = BottomTab.Calendar
                    }
                )
            }
        }
        "news_list" -> {
            currentUser?.let { user ->
                NewsListScreen(
                    news = newsUiState.news,
                    isLoading = newsUiState.isLoading,
                    errorMessage = newsUiState.errorMessage,
                    currentUserEmail = user.email,
                    onNewsSelected = { news ->
                        selectedNews = news
                        currentScreen = "news_detail"
                    },
                    onCreateNews = {
                        selectedNews = null
                        currentScreen = "news_form"
                    },
                    onEditNews = { news ->
                        selectedNews = news
                        currentScreen = "news_form"
                    },
                    onDeleteNews = { news ->
                        newsViewModel.deleteNews(news.id)
                    },
                    onBack = {
                        currentScreen = "home"
                        selectedBottomTab = BottomTab.Home
                    }
                )
            }
        }
        "news_detail" -> {
            selectedNews?.let { news ->
                currentUser?.let { user ->
                    NewsDetailScreen(
                        news = news,
                        currentUserEmail = user.email,
                        onEditNews = {
                            currentScreen = "news_form"
                        },
                        onDeleteNews = {
                            showNewsDeleteConfirmation = true
                        },
                        onBack = {
                            selectedNews = null
                            currentScreen = "news_list"
                            selectedBottomTab = BottomTab.Favorites
                        }
                    )
                }
            }
        }
        "news_form" -> {
            NewsFormScreen(
                news = selectedNews,
                isLoading = newsUiState.isLoading,
                errorMessage = newsUiState.errorMessage,
                onSaveNews = { newsToSave ->
                    if (selectedNews == null) {
                        // Creating new news
                        newsViewModel.createNews(
                            title = newsToSave.title,
                            description = newsToSave.description,
                            summary = newsToSave.summary,
                            imageUrl = newsToSave.imageUrl,
                            type = newsToSave.type,
                            keywords = newsToSave.keywords,
                            authorName = currentUser?.name ?: "Usuario",
                            isPublished = newsToSave.isPublished
                        )
                    } else {
                        // Updating existing news
                        isUpdatingNews = true
                        newsViewModel.updateNews(newsToSave)
                    }
                },
                onBack = {
                    newsViewModel.clearErrorMessage()
                    isUpdatingNews = false
                    currentScreen = if (selectedNews == null) "news_list" else "news_detail"
                    selectedBottomTab = BottomTab.Favorites
                }
            )
        }
        "chat" -> {
            currentUser?.let { user ->
                ChatScreen(
                    currentUserEmail = user.email,
                    chatId = null,
                    onBack = {
                        currentScreen = "home"
                        selectedBottomTab = BottomTab.Home
                    }
                )
            }
        }
        "profile" -> {
            currentUser?.let { user ->
                val coroutineScope = rememberCoroutineScope()
                ProfileScreen(
                    user = user,
                    paraVolver = {
                        currentScreen = "home"
                        selectedBottomTab = BottomTab.Home
                    },
                    paraCerrarSesion = {
                        coroutineScope.launch {
                            userRepository.logout()
                            currentUser = null
                            loginVmKey += 1
                            currentScreen = "login"
                            selectedBottomTab = BottomTab.Home
                        }
                    },
                    paraActualizarNombreUsuario = { newName ->
                        currentUser = user.copy(name = newName)
                    },
                    paraActualizarPassword = { currentPwd, newPwd ->
                        coroutineScope.launch {
                            val result = userRepository.updatePassword(currentPwd, newPwd)
                            result.onSuccess { println("Password updated") }
                                .onFailure { e -> println("Password update failed: ${e.message}") }
                        }
                    }
                )
            }
        }
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteConfirmation && selectedReservation != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirmar eliminación") },
            text = { 
                Text("¿Estás seguro de que deseas eliminar la reservación de ${selectedReservation!!.userName}?") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        reservationViewModel.deleteReservation(selectedReservation!!.id)
                        selectedReservation = null
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Eliminar", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // News Delete Confirmation Dialog
    if (showNewsDeleteConfirmation && selectedNews != null) {
        AlertDialog(
            onDismissRequest = { showNewsDeleteConfirmation = false },
            title = { Text("Confirmar eliminación") },
            text = { 
                Text("¿Seguro que deseas eliminar la noticia \"${selectedNews!!.title}\"?") 
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        newsViewModel.deleteNews(selectedNews!!.id)
                        selectedNews = null
                        showNewsDeleteConfirmation = false
                        currentScreen = "news_list"
                    }
                ) {
                    Text("Eliminar", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewsDeleteConfirmation = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
