package com.test.tadia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.tadia.data.User
import com.test.tadia.ui.theme.TadIATheme
import com.test.tadia.viewmodel.LoginViewModel
import com.test.tadia.viewmodel.RegisterViewModel

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
    
    val application = LocalContext.current.applicationContext as TadIAApplication
    val userRepository = application.userRepository
    
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
                    }
                )
            }
        }
    }
}
