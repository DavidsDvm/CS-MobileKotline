package com.test.tadia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.tadia.data.User

@Composable
fun ProfileScreen(
    user: User,
    paraVolver: () -> Unit,
    paraCerrarSesion: () -> Unit,
    paraActualizarNombreUsuario: (String) -> Unit,
    paraActualizarPassword: (String, String) -> Unit
) {
    var editarNombre by remember { mutableStateOf(user.name) }
    var actualPassword by remember { mutableStateOf("") }
    var nuevaPassword by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = editarNombre,
            onValueChange = { editarNombre = it },
            label = { Text("Nombre") }
        )
        Spacer(Modifier.height(8.dp))
        Text(text = "Email: ${user.email}")
        Spacer(Modifier.height(32.dp))
        Button(onClick = { paraActualizarNombreUsuario(editarNombre) }) {
            Text("Guardar cambios")
        }
        Spacer(Modifier.height(12.dp))
        Spacer(Modifier.height(24.dp))
        // cambiar la password
        Text(text = "Cambiar contraseña", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = actualPassword,
            onValueChange = { actualPassword = it },
            label = { Text("Contraseña actual") }
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = nuevaPassword,
            onValueChange = { nuevaPassword = it },
            label = { Text("Nueva contraseña") }
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmarPassword,
            onValueChange = { confirmarPassword = it },
            label = { Text("Confirmar nueva contraseña") }
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if (nuevaPassword.isNotBlank() && confirmarPassword == confirmarPassword && actualPassword.isNotBlank()) {
                    paraActualizarPassword(actualPassword, nuevaPassword)
                    actualPassword = ""
                    nuevaPassword = ""
                    confirmarPassword = ""
                }
            }
        ) {
            Text("Cambiar contraseña")
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = paraCerrarSesion) {
            Text("Cerrar sesión")
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = paraVolver) {
            Text("Volver")
        }
    }
}


