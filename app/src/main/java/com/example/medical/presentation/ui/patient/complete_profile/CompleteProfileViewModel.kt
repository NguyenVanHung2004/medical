package com.example.medical.presentation.ui.patient.complete_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.example.medical.domain.repository.ProfileRepository
import com.example.medical.domain.model.Result

class CompleteProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompleteProfileUiState())
    val uiState: StateFlow<CompleteProfileUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, errorMessage = null, invalidFields = it.invalidFields - "fullName") }
    }

    fun onDateOfBirthChange(value: String) {
        _uiState.update { it.copy(dateOfBirth = value, errorMessage = null, invalidFields = it.invalidFields - "dateOfBirth") }
    }

    fun onGenderChange(value: String) {
        _uiState.update { it.copy(gender = value, errorMessage = null, invalidFields = it.invalidFields - "gender") }
    }

    fun onAddressChange(value: String) {
        _uiState.update { it.copy(address = value, errorMessage = null, invalidFields = it.invalidFields - "address") }
    }
    
    fun onInsuranceEnabledChange(enabled: Boolean) {
        _uiState.update { it.copy(isInsuranceEnabled = enabled, invalidFields = it.invalidFields - "insuranceProvider" - "insuranceCode") }
    }

    fun onInsuranceProviderChange(value: String) {
        _uiState.update { it.copy(insuranceProvider = value, errorMessage = null, invalidFields = it.invalidFields - "insuranceProvider") }
    }

    fun onInsuranceCodeChange(value: String) {
        _uiState.update { it.copy(insuranceCode = value, errorMessage = null, invalidFields = it.invalidFields - "insuranceCode") }
    }

    fun onAgreementChange(isAgreed: Boolean) {
        _uiState.update { it.copy(isAgreedToPolicy = isAgreed, errorMessage = null, invalidFields = it.invalidFields - "agreement") }
    }

    fun submitProfile() {
        val state = _uiState.value
        val invalidFields = mutableSetOf<String>()
        
        if (state.fullName.isBlank()) invalidFields.add("fullName")
        if (state.dateOfBirth.isBlank()) invalidFields.add("dateOfBirth")
        if (state.gender.isBlank()) invalidFields.add("gender")
        if (state.address.isBlank()) invalidFields.add("address")
        if (state.isInsuranceEnabled) {
            if (state.insuranceProvider.isBlank()) invalidFields.add("insuranceProvider")
            if (state.insuranceCode.isBlank()) invalidFields.add("insuranceCode")
        }
        if (!state.isAgreedToPolicy) invalidFields.add("agreement")
        
        if (invalidFields.isNotEmpty()) {
            _uiState.update { it.copy(invalidFields = invalidFields, errorMessage = "Vui lòng hoàn thiện các thông tin còn thiếu") }
            return
        }

        viewModelScope.launch {
            profileRepository.updateProfile(
                fullName = state.fullName,
                phone = "", // Màn này không nhập phone
                dob = state.dateOfBirth,
                gender = state.gender,
                address = state.address,
                bloodType = null,
                allergies = null,
                insuranceInfo = if (state.insuranceProvider.isNotBlank() && state.insuranceCode.isNotBlank()) "${state.insuranceProvider}: ${state.insuranceCode}" else null
            ).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Result.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    is Result.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }
}
