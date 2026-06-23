package com.example.medical.presentation.ui.auth.forgot_password

data class ForgotPasswordUiState(
    val emailOrPhone: String = "",
    val otp: String = "",
    val newPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isStep1Success: Boolean = false,
    val isStep2Success: Boolean = false,
    val isStep3Success: Boolean = false,
    val passwordVisible: Boolean = false
)
