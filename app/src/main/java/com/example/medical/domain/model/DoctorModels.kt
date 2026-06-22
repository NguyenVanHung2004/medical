package com.example.medical.domain.model

data class Doctor(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val specialty: String = "",
    val experience: String = "",
    val isOnlineConsultationEnabled: Boolean = false,
    val onlineConsultationFee: Long = 0,
    val isInPersonConsultationEnabled: Boolean = false,
    val inPersonConsultationFee: Long = 0,
    val workingHoursSummary: String = "Thứ 2 - Thứ 6\n08:00 - 11:30\n13:30 - 17:00"
)

data class WorkingTimeSlot(
    val time: String,
    val isSelected: Boolean,
    val isAvailable: Boolean = true
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
