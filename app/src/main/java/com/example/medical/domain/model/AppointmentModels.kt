package com.example.medical.domain.model

enum class AppointmentType {
    ONLINE, OFFLINE
}

data class Appointment(
    val id: String,
    val doctor: Doctor,
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
