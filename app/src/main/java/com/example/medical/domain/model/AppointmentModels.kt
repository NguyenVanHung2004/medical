package com.example.medical.domain.model

import com.google.gson.annotations.SerializedName

// Tham chiếu ConsultationType từ DoctorModels (hoặc Profile) 
// Enum AppointmentStatus cũng từ DoctorModels. 
typealias AppointmentType = ConsultationType

data class AppointmentDomainModel(
    val id: String,
    val patientId: String,
    val doctorId: String,
    val type: ConsultationType,
    val status: AppointmentStatus,
    val date: String,
    val startTime: String,
    val endTime: String,
    val reason: String,
    val location: String?,
    val paymentStatus: String?
)

data class Appointment(
    val id: String,
    val doctor: Doctor,
    val patientName: String,
    val patientInitial: String,
    val patientGender: String,
    val patientAge: Int,
    @SerializedName("patientId")
    val patientIdStr: String,
    val patientAvatarUrl: String?,
    val date: String,
    val timeRange: String,
    val reason: String,
    val location: String?,
    val status: AppointmentStatus,
    val type: ConsultationType
)
