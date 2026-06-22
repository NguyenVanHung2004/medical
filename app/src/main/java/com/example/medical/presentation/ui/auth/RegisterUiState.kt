package com.example.medical.presentation.ui.auth

data class RegisterUiState(
    val selectedTab: Int = 0, // 0: Email, 1: Phone
    val email: String = "",
    val phone: String = "",
    val otp: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isDoctor: Boolean = false,
    val errorMessage: String? = null
)
