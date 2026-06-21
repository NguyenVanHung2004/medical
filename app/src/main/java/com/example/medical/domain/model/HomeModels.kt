package com.example.medical.domain.model

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val hospital: String,
    val avatarUrl: String
)

data class Appointment(
    val id: String,
    val doctor: Doctor,
    val date: String,
    val time: String,
    val hoursRemaining: Int
)

data class Specialty(
    val id: String,
    val name: String,
    val iconRes: Int? = null 
)

data class Article(
    val id: String,
    val title: String,
    val category: String,
    val imageUrl: String
)
