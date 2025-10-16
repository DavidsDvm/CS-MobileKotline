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
import com.test.tadia.data.User
import com.test.tadia.data.Room
import com.test.tadia.data.Reservation
import com.test.tadia.ui.theme.TadIATheme
import com.test.tadia.viewmodel.LoginViewModel
import com.test.tadia.viewmodel.RegisterViewModel
import com.test.tadia.viewmodel.ReservationViewModel
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    
    val application = LocalContext.current.applicationContext as TadIAApplication
    val userRepository = application.userRepository
    val reservationViewModel: ReservationViewModel = viewModel { ReservationViewModel() }
    
    // Collect UI state at the top level
    val reservationUiState by reservationViewModel.uiState.collectAsState()
    
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
                ReservationDetailsScreen(
                    room = room,
                    selectedDate = selectedDate,
                    reservations = reservationUiState.reservations,
                    timeSlots = reservationUiState.timeSlots,
                    selectedReservation = selectedReservation,
                    onDateSelected = { date ->
                        selectedDate = date
                        reservationViewModel.selectDate(date)
                    },
                    onReservationSelected = { reservation ->
                        selectedReservation = reservation
                    },
                    onEditReservation = { reservation ->
                        selectedReservation = reservation
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
                        currentScreen = "reservation_form"
                    },
                    onBack = {
                        selectedReservation = null
                        currentScreen = "calendar"
                    }
                )
            }
        }
        "reservation_form" -> {
            selectedRoom?.let { room ->
                ReservationFormScreen(
                    room = room,
                    selectedDate = selectedDate,
                    reservation = selectedReservation,
                    onSaveReservation = { reservation ->
                        if (selectedReservation == null) {
                            // Creating new reservation
                            reservationViewModel.createReservation(
                                roomId = reservation.roomId,
                                roomName = reservation.roomName,
                                userName = reservation.userName,
                                userEmail = reservation.userEmail,
                                date = reservation.date,
                                startTime = reservation.startTime,
                                endTime = reservation.endTime,
                                purpose = reservation.purpose,
                                isRecurring = reservation.isRecurring,
                                recurringPattern = reservation.recurringPattern
                            )
                        } else {
                            // Updating existing reservation
                            reservationViewModel.updateReservation(reservation)
                        }
                        selectedReservation = null
                        currentScreen = "calendar"
                    },
                    onBack = {
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
