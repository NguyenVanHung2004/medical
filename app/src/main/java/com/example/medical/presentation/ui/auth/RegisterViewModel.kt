package com.example.medical.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.model.Result
import com.example.medical.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onTabChange(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex, errorMessage = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(phone = phone, errorMessage = null) }
    }

    fun onOtpChange(otp: String) {
        _uiState.update { it.copy(otp = otp, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onPasswordVisibilityChange(isVisible: Boolean) {
        _uiState.update { it.copy(passwordVisible = isVisible) }
    }

    fun onDoctorRoleChange(isDoctor: Boolean) {
        _uiState.update { it.copy(isDoctor = isDoctor) }
    }

    fun onGoogleLoginSuccess(token: String) {
        _uiState.update { it.copy(isSuccess = true, errorMessage = null) }
    }

    fun register() {
        val currentState = _uiState.value
        viewModelScope.launch {
            registerUseCase(
                email = if (currentState.selectedTab == 0) currentState.email else "",
                phone = if (currentState.selectedTab == 1) currentState.phone else "",
                password = if (currentState.selectedTab == 0) currentState.password else currentState.otp,
                isPhoneTab = currentState.selectedTab == 1
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }
                }
            }
        }
    }

    fun resetState() {
        _uiState.update { RegisterUiState() }
    }
}
