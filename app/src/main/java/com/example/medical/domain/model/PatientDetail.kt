package com.example.medical.domain.model

data class PatientDetail(
    val id: String,
    val fullName: String,
    val avatarUrl: String?,
    val phone: String,
    val email: String,
    val dob: String,
    val gender: String,
    val address: String,
    val bloodType: String?,
    val allergies: String?
)
