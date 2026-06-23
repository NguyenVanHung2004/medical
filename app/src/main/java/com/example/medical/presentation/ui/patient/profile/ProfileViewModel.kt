package com.example.medical.presentation.ui.patient.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getUserProfile().collect { (user, patientProfile) ->
                val uiModel = UserProfileUiModel(
                    id = user.id,
                    fullName = user.fullName,
                    dob = patientProfile.dob,
                    gender = patientProfile.gender,
                    email = user.email,
                    phone = user.phone ?: "",
                    address = patientProfile.address,
                    bloodType = patientProfile.bloodType,
                    allergies = patientProfile.allergies,
                    avatarUrl = user.avatarUrl
                )
                _uiState.update { it.copy(profile = uiModel, isLoading = false) }
            }
        }
    }
}
