package com.test.tadia.data

import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

// Extension functions to convert between LocalDate/LocalTime and String
fun LocalDate.toDatabaseString(): String = this.toString()
fun String.toLocalDate(): LocalDate = LocalDate.parse(this)

fun LocalTime.toDatabaseString(): String = this.toString()
fun String.toLocalTime(): LocalTime = LocalTime.parse(this)

fun ReservationStatus.toDatabaseString(): String = this.name
fun String.toReservationStatus(): ReservationStatus = ReservationStatus.valueOf(this)

fun RecurringPattern.toDatabaseString(): String = this.name
fun String.toRecurringPattern(): RecurringPattern = RecurringPattern.valueOf(this)

// Helper functions to create Reservation with proper types
fun createReservation(
    id: String,
    roomId: String,
    roomName: String,
    userName: String,
    userEmail: String,
    date: LocalDate,
    startTime: LocalTime,
    endTime: LocalTime,
    purpose: String,
    isRecurring: Boolean = false,
    recurringPattern: RecurringPattern? = null,
    status: ReservationStatus = ReservationStatus.CONFIRMED,
    createdAt: Date = Date(),
    createdByEmail: String
): Reservation {
    return Reservation(
        id = id,
        roomId = roomId,
        roomName = roomName,
        userName = userName,
        userEmail = userEmail,
        date = date.toDatabaseString(),
        startTime = startTime.toDatabaseString(),
        endTime = endTime.toDatabaseString(),
        purpose = purpose,
        isRecurring = isRecurring,
        recurringPattern = recurringPattern?.toDatabaseString(),
        status = status.toDatabaseString(),
        createdAt = createdAt,
        createdByEmail = createdByEmail
    )
}

// Helper functions to get typed values from database Reservation
fun Reservation.getDate(): LocalDate = this.date.toLocalDate()
fun Reservation.getStartTime(): LocalTime = this.startTime.toLocalTime()
fun Reservation.getEndTime(): LocalTime = this.endTime.toLocalTime()
fun Reservation.getStatus(): ReservationStatus = this.status.toReservationStatus()
fun Reservation.getRecurringPattern(): RecurringPattern? = this.recurringPattern?.toRecurringPattern()

// Helper function to check if user can edit/delete reservation
fun Reservation.canUserEdit(userEmail: String): Boolean {
    return this.createdByEmail == userEmail
}
