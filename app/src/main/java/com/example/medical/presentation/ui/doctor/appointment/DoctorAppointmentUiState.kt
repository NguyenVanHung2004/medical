package com.example.medical.presentation.ui.doctor.appointment

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest

data class DoctorAppointmentUiState(
    val isLoading: Boolean = false,
    val pendingRequests: List<AppointmentRequest> = emptyList(),
    val scheduledAppointments: List<Appointment> = emptyList(),
    val errorMessage: String? = null
)
