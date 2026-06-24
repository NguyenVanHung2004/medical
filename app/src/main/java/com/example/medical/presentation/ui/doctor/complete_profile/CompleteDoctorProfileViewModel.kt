package com.example.medical.presentation.ui.doctor.complete_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.repository.DoctorProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CompleteDoctorProfileUiState(
    val fullName: String = "",
    val specialty: String = "",
    val hospital: String = "",
    val experience: String = "",
    val bio: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class CompleteDoctorProfileViewModel(
    private val repository: DoctorProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompleteDoctorProfileUiState())
    val uiState: StateFlow<CompleteDoctorProfileUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun onSpecialtyChange(value: String) {
        _uiState.update { it.copy(specialty = value, errorMessage = null) }
    }

    fun onHospitalChange(value: String) {
        _uiState.update { it.copy(hospital = value, errorMessage = null) }
    }

    fun onExperienceChange(value: String) {
        _uiState.update { it.copy(experience = value, errorMessage = null) }
    }

    fun onBioChange(value: String) {
        _uiState.update { it.copy(bio = value, errorMessage = null) }
    }

    fun submitProfile() {
        val state = _uiState.value
        if (state.fullName.isBlank() || state.specialty.isBlank() || state.hospital.isBlank() || state.experience.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.updateProfile(
                name = state.fullName,
                specialty = state.specialty,
                hospital = state.hospital,
                experience = state.experience,
                bio = state.bio
            ).collect { success ->
                if (success) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Cập nhật thất bại") }
                }
            }
        }
    }
}
