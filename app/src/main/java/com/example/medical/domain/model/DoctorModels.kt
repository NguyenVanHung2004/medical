package com.example.medical.domain.model

data class Doctor(
    val id: String,
    val name: String,
    val avatarUrl: String?
)

data class AppointmentRequest(
    val id: String,
    val patientName: String,
    val patientInitial: String,
    val timeRange: String,
    val reason: String
)

enum class AppointmentStatus {
    HAPPENING, UPCOMING, COMPLETED
}

enum class AppointmentType {
    ONLINE, OFFLINE
}

data class Appointment(
    val id: String,
    val patientName: String,
    val patientInitial: String,
    val patientGender: String,
    val patientAge: Int,
    val patientIdStr: String,
    val patientAvatarUrl: String?,
    val date: String,
    val timeRange: String,
    val reason: String,
    val location: String?,
    val status: AppointmentStatus,
    val type: AppointmentType
)
