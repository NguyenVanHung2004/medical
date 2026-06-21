package com.example.medical.presentation.ui.doctor_list

import com.example.medical.domain.model.DoctorDetail

data class DoctorListUiState(
    val isLoading: Boolean = false,
    val title: String = "Danh sách Bác sĩ",
    val searchQuery: String = "",
    val doctors: List<DoctorDetail> = emptyList(),
    val error: String? = null,
    val selectedSpecialty: String? = null,
    val selectedLocation: String? = null,
    val selectedRating: String? = null,
    val selectedAvailability: String? = null,
    val selectedInsurance: String? = null
)
