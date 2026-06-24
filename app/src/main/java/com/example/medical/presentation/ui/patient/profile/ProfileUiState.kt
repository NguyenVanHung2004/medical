package com.example.medical.presentation.ui.patient.profile

data class UserProfileUiModel(
    val id: String,
    val fullName: String,
    val dob: String,
    val gender: String,
    val email: String,
    val phone: String,
    val address: String,
    val bloodType: String?,
    val allergies: String?,
    val avatarUrl: String?
)

data class ProfileUiState(
    val profile: UserProfileUiModel? = null,
    val isLoading: Boolean = false,
    val showEditDialog: Boolean = false,
    val isSubmitting: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)
