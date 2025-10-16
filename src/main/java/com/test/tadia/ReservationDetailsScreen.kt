package com.test.tadia

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.tadia.data.Reservation
import com.test.tadia.data.Room
import com.test.tadia.data.TimeSlot
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDetailsScreen(
    room: Room,
    selectedDate: LocalDate,
    reservations: List<Reservation>,
    timeSlots: List<TimeSlot>,
    selectedReservation: Reservation?,
    onDateSelected: (LocalDate) -> Unit,
    onReservationSelected: (Reservation) -> Unit,
    onEditReservation: (Reservation) -> Unit,
    onDeleteReservation: (Reservation) -> Unit,
    onShowDeleteConfirmation: (Reservation) -> Unit,
    onNewReservation: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Agendamiento de espacios",
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
                .verticalScroll(rememberScrollState())
        ) {
            // Selected room indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Default.Circle,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(8.dp)
                )
            }

            // Calendar
            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = onDateSelected,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Daily schedule
            Text(
                text = "Programación del Día",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Schedule timeline with selected reservation
            DailyScheduleViewWithSelection(
                timeSlots = timeSlots,
                reservations = reservations,
                selectedReservation = selectedReservation,
                onReservationSelected = onReservationSelected,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(24.dp))

            // CRUD Action buttons
            if (selectedReservation != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Edit button
                    Button(
                        onClick = { onEditReservation(selectedReservation) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3) // Blue color
                        )
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Editar",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    // Delete button
                    Button(
                        onClick = { onShowDeleteConfirmation(selectedReservation) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44336) // Red color
                        )
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Eliminar",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            } else {
                // New reservation button
                Button(
                    onClick = onNewReservation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3) // Blue color
                    )
                ) {
                    Text(
                        text = "Realizar Agendamiento",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val startDate = today.minusDays(7)
    val endDate = today.plusDays(14)
    
    val dates = (0..21).map { startDate.plusDays(it.toLong()) }
    
    Column(modifier = modifier) {
        // Day headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Calendar dates
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(dates.size) { index ->
                val date = dates[index]
                val isSelected = date == selectedDate
                val isToday = date == today
                val isPastDate = date.isBefore(today)
                
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isSelected -> Color(0xFF2196F3)
                                isToday -> Color(0xFFE3F2FD)
                                isPastDate -> Color(0xFFF5F5F5) // Gray background for past dates
                                else -> Color.Transparent
                            }
                        )
                        .clickable(enabled = !isPastDate) { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp,
                            color = when {
                                isSelected -> Color.White
                                isToday -> Color(0xFF1976D2)
                                isPastDate -> Color(0xFFBDBDBD) // Gray text for past dates
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyScheduleViewWithSelection(
    timeSlots: List<TimeSlot>,
    reservations: List<Reservation>,
    selectedReservation: Reservation?,
    onReservationSelected: (Reservation) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        timeSlots.forEach { slot ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time label
                Text(
                    text = slot.startTime.toString().substring(0, 5),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.width(60.dp)
                )
                
                Spacer(Modifier.width(16.dp))
                
                // Reservation card or empty space
                if (slot.reservation != null) {
                    ReservationCardWithSelection(
                        reservation = slot.reservation,
                        isSelected = selectedReservation?.id == slot.reservation.id,
                        onClick = { onReservationSelected(slot.reservation!!) }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF5F5F5))
                    )
                }
            }
        }
    }
}

@Composable
private fun ReservationCardWithSelection(
    reservation: Reservation,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = Color(0xFFFFC107), // Yellow border for selection
                        shape = RoundedCornerShape(8.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD) // Light blue
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = reservation.userName,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            )
            
            Spacer(Modifier.height(4.dp))
            
            Text(
                text = "${reservation.startTime.toString().substring(0, 5)} - ${reservation.endTime.toString().substring(0, 5)}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            
            if (reservation.isRecurring) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Todos los ${getDayOfWeek(reservation.date)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = Color(0xFF1976D2)
                    )
                )
            } else {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Solo Hoy",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = Color(0xFF4CAF50)
                    )
                )
            }
        }
    }
}

private fun getDayOfWeek(date: LocalDate): String {
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
}

@Preview(showBackground = true)
@Composable
private fun ReservationDetailsScreenPreview() {
    MaterialTheme {
        ReservationDetailsScreen(
            room = com.test.tadia.data.Room(
                id = "1",
                name = "Sala de Cine",
                description = "Sala equipada con proyector",
                capacity = 50
            ),
            selectedDate = LocalDate.now(),
            reservations = emptyList(),
            timeSlots = emptyList(),
            selectedReservation = null,
            onDateSelected = {},
            onReservationSelected = {},
            onEditReservation = {},
            onDeleteReservation = {},
            onShowDeleteConfirmation = {},
            onNewReservation = {},
            onBack = {}
        )
    }
}
