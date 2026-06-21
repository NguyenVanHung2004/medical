package com.example.medical.domain.model

enum class ConsultationType {
    ONLINE, OFFLINE
}

data class DoctorDetail(
    val id: String,
    val name: String,
    val specialty: String,
    val hospital: String,
    val avatarUrl: String,
    val rating: Double,
    val yearsOfExperience: Int,
    val isOnline: Boolean,
    val supportedTypes: List<ConsultationType>,
    val isFullyBookedToday: Boolean = false
)
