package com.example.medical.presentation.ui.patient.appointments

import com.example.medical.domain.model.Appointment

data class AppointmentUiState(
  val appointments: List<Appointment>
)