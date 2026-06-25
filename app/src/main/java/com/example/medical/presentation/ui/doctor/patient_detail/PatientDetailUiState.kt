package com.example.medical.presentation.ui.doctor.patient_detail

import com.example.medical.domain.model.PatientDetail

data class PatientDetailUiState(
    val isLoading: Boolean = false,
    val patientDetail: PatientDetail? = null,
    val errorMessage: String? = null
)
