package com.example.medical.domain.model

data class PatientProfile(
    val userId: String,
    val dob: String,
    val gender: String,
    val address: String,
    val bloodType: String?,
    val allergies: String?,
    val insuranceInfo: String?
)

data class DoctorProfile(
    val userId: String,
    val specialty: String,
    val hospital: String,
    val bio: String,
    val yearsOfExperience: Int,
    val rating: Double,
    val reviewCount: Int,
    val consultationOfferings: List<ConsultationOffering>
)

data class ConsultationOffering(
    val type: ConsultationType,
    val fee: Long,
    val isEnabled: Boolean
)
