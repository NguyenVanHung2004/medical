package com.example.medical.domain.model


//data class Appointment(
//    val id: String,
//    val doctor: Doctor,
//    val date: String,
//    val time: String,
//    val hoursRemaining: Int,
//    val location: String = "Tư vấn trực tuyến",
//    val isOnline: Boolean = true,
//    val status: String? = null,
//    val reason: String? = null,
//    val notes: String? = null
//)

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
