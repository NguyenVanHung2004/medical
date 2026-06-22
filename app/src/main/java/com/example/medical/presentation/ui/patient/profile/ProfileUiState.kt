package com.example.medical.presentation.ui.patient.profile

import com.example.medical.domain.model.UserProfile

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false
)
