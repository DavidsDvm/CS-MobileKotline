package com.test.tadia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.test.tadia.ui.theme.TadIATheme

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
    
    when (currentScreen) {
        "login" -> LoginScreen(
            onLogin = { user, pass ->
                // TODO: Implement login logic
                println("Login attempt: $user")
            },
            onRegister = {
                currentScreen = "register"
            },
            onLoginWithOutlook = {
                // TODO: Implement Outlook login
                println("Outlook login")
            }
        )
        "register" -> RegisterScreen(
            onRegister = { email, name, password ->
                // TODO: Implement registration logic
                println("Register attempt: $email, $name")
                // After successful registration, go back to login
                currentScreen = "login"
            },
            onBackToLogin = {
                currentScreen = "login"
            }
        )
    }
}
