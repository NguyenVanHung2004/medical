package com.example.medical.domain.model

data class UserProfile(
    val id: String,
    val fullName: String,
    val dob: String,
    val gender: String,
    val email: String,
    val phone: String,
    val address: String,
    val bloodType: String,
    val allergies: String,
    val avatarUrl: String
)
