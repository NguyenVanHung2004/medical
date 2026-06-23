package com.example.medical.presentation.ui.patient.appointments

import com.example.medical.domain.model.Appointment

data class AppointmentsUiState(
    val upcomingAppointments: List<Appointment> = emptyList(),
    val historyAppointments: List<Appointment> = emptyList(),
    val isLoading: Boolean = false,
    val appointmentToCancel: String? = null,
    val error: String? = null
)
