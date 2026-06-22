package com.example.medical.domain.model


data class DoctorNotification(
    val id: String,
    val type: NotificationType,
    val patientName: String,
    val timeInfo: String,
    val timestamp: String,
    val isRead: Boolean,
    val appointmentType: AppointmentType? = null // To distinguish online/offline for UPCOMING_APPOINTMENT
)
