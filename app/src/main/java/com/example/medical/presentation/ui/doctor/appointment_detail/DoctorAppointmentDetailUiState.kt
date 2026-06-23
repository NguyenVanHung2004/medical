package com.example.medical.presentation.ui.doctor.appointment_detail

import com.example.medical.domain.model.Appointment

data class DoctorAppointmentDetailUiState(
    val isLoading: Boolean = false,
    val appointment: Appointment? = null,
    val errorMessage: String? = null
)
