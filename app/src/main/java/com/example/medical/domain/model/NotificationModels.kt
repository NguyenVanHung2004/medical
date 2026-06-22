package com.example.medical.domain.model

enum class NotificationType {
    NEW_APPOINTMENT_REQUEST,
    APPOINTMENT_CANCELLED,
    UPCOMING_APPOINTMENT
}

data class DoctorNotification(
    val id: String,
    val type: NotificationType,
    val patientName: String,
    val timeInfo: String,
    val timestamp: String,
    val isRead: Boolean,
    val appointmentType: AppointmentType? = null // To distinguish online/offline for UPCOMING_APPOINTMENT
)
