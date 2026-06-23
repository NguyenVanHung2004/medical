package com.example.medical.presentation.ui.patient.appointment_detail

import com.example.medical.domain.model.Appointment

data class AppointmentDetailUiState(
    val appointment: Appointment? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showCancelDialog: Boolean = false,
    val isCancelled: Boolean = false
)
