package com.example.medical.presentation.ui.patient.doctor_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.model.ConsultationType
import com.example.medical.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorListViewModel(
    private val repository: DoctorRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val typeParam: String? = savedStateHandle["type"]
    private val specialtyParam: String? = savedStateHandle["specialty"]

    private val _uiState = MutableStateFlow(
        DoctorListUiState(
            selectedSpecialty = specialtyParam
        )
    )
    val uiState: StateFlow<DoctorListUiState> = _uiState.asStateFlow()

    init {
        val consultationType = when (typeParam) {
            "online" -> ConsultationType.ONLINE
            "offline" -> ConsultationType.OFFLINE
            else -> null
        }

        val title = when (typeParam) {
            "online" -> "Khám trực tuyến"
            "offline" -> "Khám trực tiếp"
            else -> "Danh sách Bác sĩ"
        }

        _uiState.update { it.copy(title = title) }
        loadDoctors(consultationType)
    }

    private fun loadDoctors(type: ConsultationType?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getDoctors(type).collect { list ->
                _uiState.update { it.copy(doctors = list, isLoading = false) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onSpecialtySelected(specialty: String?) {
        _uiState.update { it.copy(selectedSpecialty = specialty) }
        // Implement local filtering logic here if needed
    }

    fun onLocationSelected(location: String?) {
        _uiState.update { it.copy(selectedLocation = location) }
    }

    fun onRatingSelected(rating: String?) {
        _uiState.update { it.copy(selectedRating = rating) }
    }

    fun onAvailabilitySelected(availability: String?) {
        _uiState.update { it.copy(selectedAvailability = availability) }
    }

    fun onInsuranceSelected(insurance: String?) {
        _uiState.update { it.copy(selectedInsurance = insurance) }
    }
}