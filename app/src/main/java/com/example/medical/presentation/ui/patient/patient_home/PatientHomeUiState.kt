package com.example.medical.presentation.ui.patient.patient_home

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.Article
import com.example.medical.domain.model.Specialty

data class PatientHomeUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val upcomingAppointment: Appointment? = null,
    val specialties: List<Specialty> = emptyList(),
    val articles: List<Article> = emptyList(),
    val error: String? = null
)
