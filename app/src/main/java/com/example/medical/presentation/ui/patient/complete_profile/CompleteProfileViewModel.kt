package com.example.medical.presentation.ui.patient.complete_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompleteProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompleteProfileUiState())
    val uiState: StateFlow<CompleteProfileUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun onDateOfBirthChange(value: String) {
        _uiState.update { it.copy(dateOfBirth = value, errorMessage = null) }
    }

    fun onGenderChange(value: String) {
        _uiState.update { it.copy(gender = value, errorMessage = null) }
    }

    fun onAddressChange(value: String) {
        _uiState.update { it.copy(address = value, errorMessage = null) }
    }

    fun onInsuranceProviderChange(value: String) {
        _uiState.update { it.copy(insuranceProvider = value, errorMessage = null) }
    }

    fun onInsuranceCodeChange(value: String) {
        _uiState.update { it.copy(insuranceCode = value, errorMessage = null) }
    }

    fun onAgreementChange(isAgreed: Boolean) {
        _uiState.update { it.copy(isAgreedToPolicy = isAgreed, errorMessage = null) }
    }

    fun submitProfile() {
        val state = _uiState.value
        if (state.fullName.isBlank() || state.dateOfBirth.isBlank() || state.gender.isBlank() || state.address.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin cá nhân") }
            return
        }
        if (!state.isAgreedToPolicy) {
            _uiState.update { it.copy(errorMessage = "Bạn cần đồng ý với điều khoản") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(1000)
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }
}
