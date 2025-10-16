package com.test.tadia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.tadia.data.Reservation
import com.test.tadia.data.RecurringPattern
import com.test.tadia.data.Room
import com.test.tadia.data.getStartTime
import com.test.tadia.data.getEndTime
import com.test.tadia.data.getRecurringPattern
import com.test.tadia.data.createReservation
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationFormScreen(
    room: Room,
    selectedDate: LocalDate,
    reservation: Reservation? = null, // null for new reservation, not null for editing
    errorMessage: String? = null,
    isLoading: Boolean = false,
    onSaveReservation: (Reservation) -> Unit,
    onBack: () -> Unit
) {
    var userName by remember { mutableStateOf(reservation?.userName ?: "Usuario Demo") }
    var userEmail by remember { mutableStateOf(reservation?.userEmail ?: "usuario@university.edu") }
    var purpose by remember { mutableStateOf(reservation?.purpose ?: "Reunión de trabajo") }
    var startTime by remember { mutableStateOf(reservation?.getStartTime() ?: LocalTime.of(9, 0)) }
    var endTime by remember { mutableStateOf(reservation?.getEndTime() ?: LocalTime.of(10, 0)) }
    var isRecurring by remember { mutableStateOf(reservation?.isRecurring ?: false) }
    var recurringPattern by remember { mutableStateOf(reservation?.getRecurringPattern() ?: RecurringPattern.WEEKLY) }
    
    var showTimePicker by remember { mutableStateOf(false) }
    var showRecurringDialog by remember { mutableStateOf(false) }
    var showTimeError by remember { mutableStateOf(false) }
    var showDateError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (reservation == null) "Nueva Reservación" else "Editar Reservación",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            // Room info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sala: ${room.name}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "Fecha: ${selectedDate.dayOfMonth}/${selectedDate.monthValue}/${selectedDate.year}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Error message display
            if (errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE) // Light red background
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFD32F2F), // Red text
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Form fields
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = userEmail,
                onValueChange = { userEmail = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = purpose,
                onValueChange = { purpose = it },
                label = { Text("Propósito de la reservación") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(Modifier.height(24.dp))

            // Time selection
            Text(
                text = "Horario",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Start time
                OutlinedTextField(
                    value = startTime.toString().substring(0, 5),
                    onValueChange = { },
                    label = { Text("Hora inicio") },
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    trailingIcon = {
                        TextButton(onClick = { showTimePicker = true }) {
                            Text("Seleccionar")
                        }
                    }
                )

                // End time
                OutlinedTextField(
                    value = endTime.toString().substring(0, 5),
                    onValueChange = { },
                    label = { Text("Hora fin") },
                    modifier = Modifier.weight(1f),
                    readOnly = true
                )
            }

            Spacer(Modifier.height(24.dp))

            // Recurring option
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isRecurring,
                    onCheckedChange = { isRecurring = it }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Reservación recurrente",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (isRecurring) {
                Spacer(Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = when (recurringPattern) {
                        RecurringPattern.DAILY -> "Diario"
                        RecurringPattern.WEEKLY -> "Semanal"
                        RecurringPattern.MONTHLY -> "Mensual"
                    },
                    onValueChange = { },
                    label = { Text("Frecuencia") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        TextButton(onClick = { showRecurringDialog = true }) {
                            Text("Cambiar")
                        }
                    }
                )
            }

            Spacer(Modifier.height(32.dp))

            // Date validation error message
            if (showDateError) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE) // Light red background
                    )
                ) {
                    Text(
                        text = "No se pueden crear reservaciones para fechas pasadas",
                        color = Color(0xFFD32F2F), // Red text
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // Time validation error message
            if (showTimeError) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE) // Light red background
                    )
                ) {
                    Text(
                        text = "El espacio a esta hora se encuentra cerrado",
                        color = Color(0xFFD32F2F), // Red text
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            // Save button
            Button(
                onClick = {
                    // Validate date (only future dates allowed for NEW reservations)
                    val today = LocalDate.now()
                    if (reservation == null && selectedDate.isBefore(today)) {
                        showDateError = true
                        showTimeError = false
                        return@Button
                    }
                    
                    // Validate time range (7:00 AM to 5:00 PM)
                    val openingTime = LocalTime.of(7, 0)
                    val closingTime = LocalTime.of(17, 0)
                    
                    if (startTime.isBefore(openingTime) || endTime.isAfter(closingTime) || 
                        startTime.isAfter(closingTime) || endTime.isBefore(openingTime)) {
                        showTimeError = true
                        showDateError = false
                        return@Button
                    }
                    
                    showTimeError = false
                    showDateError = false
                    
                    val newReservation = createReservation(
                        id = reservation?.id ?: System.currentTimeMillis().toString(),
                        roomId = room.id,
                        roomName = room.name,
                        userName = userName,
                        userEmail = userEmail,
                        date = selectedDate,
                        startTime = startTime,
                        endTime = endTime,
                        purpose = purpose,
                        isRecurring = isRecurring,
                        recurringPattern = if (isRecurring) recurringPattern else null,
                        createdByEmail = reservation?.createdByEmail ?: userEmail // Preserve original creator when editing
                    )
                    onSaveReservation(newReservation)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (errorMessage == null) Color(0xFF2196F3) else Color(0xFFBDBDBD) // Blue when enabled, gray when disabled
                ),
                enabled = userName.isNotBlank() && userEmail.isNotBlank() && purpose.isNotBlank() && errorMessage == null && !isLoading
            ) {
                Text(
                    text = when {
                        isLoading -> "Validando..."
                        errorMessage != null -> "Resolución no disponible"
                        reservation == null -> "Crear Reservación"
                        else -> "Actualizar Reservación"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // Time picker dialog
    if (showTimePicker) {
        TimePickerDialog(
            initialTime = startTime,
            onTimeSelected = { time ->
                startTime = time
                // Auto-adjust end time to be 1 hour later
                endTime = time.plusHours(1)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    // Recurring pattern dialog
    if (showRecurringDialog) {
        RecurringPatternDialog(
            selectedPattern = recurringPattern,
            onPatternSelected = { pattern ->
                recurringPattern = pattern
                showRecurringDialog = false
            },
            onDismiss = { showRecurringDialog = false }
        )
    }
}

@Composable
private fun TimePickerDialog(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTime by remember { mutableStateOf(initialTime) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar hora") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hora: ${String.format("%02d:%02d", selectedTime.hour, selectedTime.minute)}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                // Hour selection
                Text(
                    text = "Hora: ${selectedTime.hour}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { 
                            val newTime = selectedTime.minusHours(1)
                            if (newTime.hour >= 7) {
                                selectedTime = newTime
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        enabled = selectedTime.hour > 7,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = Color.White
                        )
                    }
                    
                    Text(
                        text = "${selectedTime.hour}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    Button(
                        onClick = { 
                            val newTime = selectedTime.plusHours(1)
                            if (newTime.hour <= 17) {
                                selectedTime = newTime
                            }
                        },
                        modifier = Modifier.size(56.dp),
                        enabled = selectedTime.hour < 17,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = Color.White
                        )
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                // Minute selection
                Text(
                    text = "Minutos: ${selectedTime.minute}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { 
                            selectedTime = selectedTime.minusMinutes(15)
                        },
                        modifier = Modifier.size(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "-15",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = Color.White
                        )
                    }
                    
                    Text(
                        text = "${selectedTime.minute}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    Button(
                        onClick = { 
                            selectedTime = selectedTime.plusMinutes(15)
                        },
                        modifier = Modifier.size(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "+15",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onTimeSelected(selectedTime) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun RecurringPatternDialog(
    selectedPattern: RecurringPattern,
    onPatternSelected: (RecurringPattern) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar frecuencia") },
        text = {
            Column {
                RecurringPattern.values().forEach { pattern ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPattern == pattern,
                            onClick = { onPatternSelected(pattern) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = when (pattern) {
                                RecurringPattern.DAILY -> "Diario"
                                RecurringPattern.WEEKLY -> "Semanal"
                                RecurringPattern.MONTHLY -> "Mensual"
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Aceptar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ReservationFormScreenPreview() {
    MaterialTheme {
        ReservationFormScreen(
            room = com.test.tadia.data.Room(
                id = "1",
                name = "Sala de Cine",
                description = "Sala equipada con proyector",
                capacity = 50
            ),
            selectedDate = LocalDate.now(),
            errorMessage = null,
            isLoading = false,
            onSaveReservation = {},
            onBack = {}
        )
    }
}
