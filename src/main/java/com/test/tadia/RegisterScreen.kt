package com.test.tadia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.tadia.ui.theme.TadIATheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onRegister: (email: String, name: String, password: String) -> Unit,
    onBackToLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val passwordStrength = remember(password) { PasswordStrength.from(password) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // University / App Logo (WebP image)
            Image(
                painter = painterResource(id = R.drawable.utadeo_logo),
                contentDescription = "UTadeo Logo",
                modifier = Modifier
                    .padding(top = 24.dp)
                    .height(56.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "TadIA",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Agendamientos–Chat con Asistente virtual",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // Back button and title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text("← Volver")
                }
                Text(
                    text = "Registrarse",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = { Text("Correo") },
                placeholder = { Text("nombre@gmail.com") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(14.dp))

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = { Text("Tu Nombre") },
                placeholder = { Text("@nombre_usuario") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                }
            )

            Spacer(Modifier.height(14.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                label = { Text("Contraseña") },
                placeholder = { Text("••••••••") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.VpnKey, contentDescription = null)
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                supportingText = {
                    PasswordStrengthBar(strength = passwordStrength)
                }
            )

            Spacer(Modifier.height(26.dp))

            Button(
                onClick = { onRegister(email.trim(), name.trim(), password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Registrarse", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(18.dp))
        }
    }
}

/* ----------------------- Password Strength UI & Logic ----------------------- */

@Composable
private fun PasswordStrengthBar(strength: PasswordStrength) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) { index ->
                val active = index < strength.level
                val color = when (strength) {
                    PasswordStrength.WEAK -> if (active) Color(0xFFFF6B6B) else Color(0x33555555)
                    PasswordStrength.MEDIUM -> if (active) Color(0xFFFFC107) else Color(0x33555555)
                    PasswordStrength.STRONG -> if (active) Color(0xFF2ECC71) else Color(0x33555555)
                }
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .height(6.dp)
                        .then(Modifier.background(color, RoundedCornerShape(8.dp)))
                )
            }
        }
        Text(
            text = strength.label,
            color = when (strength) {
                PasswordStrength.WEAK -> Color(0xFFEB5757)
                PasswordStrength.MEDIUM -> Color(0xFFB8860B)
                PasswordStrength.STRONG -> Color(0xFF2ECC71)
            },
            style = MaterialTheme.typography.labelLarge
        )
    }
}

enum class PasswordStrength(val level: Int, val label: String) {
    WEAK(1, "Débil"),
    MEDIUM(2, "Media"),
    STRONG(3, "Fuerte");

    companion object {
        fun from(pwd: String): PasswordStrength {
            var score = 0
            if (pwd.length >= 8) score++
            if (pwd.any { it.isUpperCase() } && pwd.any { it.isLowerCase() }) score++
            if (pwd.any { it.isDigit() } || pwd.any { "!@#${'$'}%^&*()_-+=[]{}|:;,.?/~`".contains(it) }) score++
            return when (score) {
                0, 1 -> WEAK
                2 -> MEDIUM
                else -> STRONG
            }
        }
    }
}

/* --------------------------------- Preview -------------------------------- */

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterPreview() {
    TadIATheme { RegisterScreen(onRegister = { _, _, _ -> }) }
}
