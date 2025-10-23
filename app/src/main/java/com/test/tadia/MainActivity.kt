package com.test.tadia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp
import com.test.tadia.data.FirebaseService
import com.test.tadia.data.User
import com.test.tadia.data.Room
import com.test.tadia.data.Reservation
import com.test.tadia.data.getDate
import com.test.tadia.data.getStartTime
import com.test.tadia.data.getEndTime
import com.test.tadia.data.getRecurringPattern
import com.test.tadia.repository.FirebaseRepository
import com.test.tadia.ui.theme.TadIATheme
import com.test.tadia.viewmodel.LoginViewModel
import com.test.tadia.viewmodel.RegisterViewModel
import com.test.tadia.viewmodel.ReservationViewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase - uncomment when you have correct google-services.json
        // FirebaseApp.initializeApp(this)
        
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
    
    // Reservation flow state
    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var isUpdatingReservation by remember { mutableStateOf(false) }
    
    val application = LocalContext.current.applicationContext as TadIAApplication
    val userRepository = application.userRepository
    val reservationRepository = application.reservationRepository
    val reservationViewModel: ReservationViewModel = viewModel { ReservationViewModel(reservationRepository) }
    
    // Collect UI state at the top level
    val reservationUiState by reservationViewModel.uiState.collectAsState()
    
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
    
    when (currentScreen) {
        "login" -> {
            val loginViewModel: LoginViewModel = viewModel {
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
                    }
                    currentScreen = "home"
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
            LaunchedEffect(registerUiState.isRegistrationSuccessful) {
                if (registerUiState.isRegistrationSuccessful) {
                    currentScreen = "login"
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
                },
                onBack = {
                    currentScreen = "home"
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
                    }
                )
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
}
