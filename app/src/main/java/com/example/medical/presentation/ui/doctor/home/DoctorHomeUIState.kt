package com.example.medical.presentation.ui.doctor.home

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.Doctor

data class DoctorHomeUIState(
    val isLoading: Boolean = false,
    val doctor: Doctor? = null,
    val pendingRequests: List<AppointmentRequest> = emptyList(),
    val todayAppointments: List<Appointment> = emptyList(),
    val errorMessage: String? = null
)