package com.example.medical.presentation.ui.patient.complete_profile

data class CompleteProfileUiState(
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val address: String = "",
    val insuranceProvider: String = "",
    val insuranceCode: String = "",
    val isInsuranceEnabled: Boolean = false,
    val isAgreedToPolicy: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val invalidFields: Set<String> = emptySet(),
    val errorMessage: String? = null
)
