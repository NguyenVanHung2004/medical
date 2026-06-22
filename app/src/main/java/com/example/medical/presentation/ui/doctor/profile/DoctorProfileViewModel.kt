package com.example.medical.presentation.ui.doctor.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.model.WorkingTimeSlot
import com.example.medical.domain.repository.DoctorProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek

class DoctorProfileViewModel(
    private val repository: DoctorProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorProfileUiState())
    val uiState: StateFlow<DoctorProfileUiState> = _uiState.asStateFlow()

    init {
        fetchDoctorProfile()
    }

    private fun fetchDoctorProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getDoctorProfile()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { doctor ->
                    _uiState.update { it.copy(isLoading = false, doctor = doctor) }
                }
        }
    }

    fun toggleOnlineConsultation(isEnabled: Boolean) {
        viewModelScope.launch {
            repository.toggleOnlineConsultation(isEnabled).collect {}
        }
    }

    fun toggleInPersonConsultation(isEnabled: Boolean) {
        viewModelScope.launch {
            repository.toggleInPersonConsultation(isEnabled).collect {}
        }
    }

    fun showWorkingHoursDialog() {
        _uiState.update { it.copy(isWorkingHoursDialogVisible = true) }
        fetchWorkingTimeSlots(_uiState.value.selectedDayOfWeek)
    }

    fun hideWorkingHoursDialog() {
        _uiState.update { it.copy(isWorkingHoursDialogVisible = false) }
    }

    fun selectDayOfWeek(dayOfWeek: DayOfWeek) {
        _uiState.update { it.copy(selectedDayOfWeek = dayOfWeek) }
        fetchWorkingTimeSlots(dayOfWeek)
    }

    fun toggleTimeSlot(timeSlot: WorkingTimeSlot) {
        val updatedSlots = _uiState.value.timeSlotsForSelectedDay.map {
            if (it.time == timeSlot.time) it.copy(isSelected = !it.isSelected)
            else it
        }
        _uiState.update { it.copy(timeSlotsForSelectedDay = updatedSlots) }
    }

    fun confirmWorkingHoursUpdate() {
        viewModelScope.launch {
            repository.updateWorkingTimeSlots(
                dayOfWeek = _uiState.value.selectedDayOfWeek,
                slots = _uiState.value.timeSlotsForSelectedDay
            ).collect {
                // To update the summary immediately, we refetch the doctor profile
                repository.getDoctorProfile().collect { doctor ->
                     _uiState.update { it.copy(doctor = doctor) }
                }
                hideWorkingHoursDialog()
            }
        }
    }

    private fun fetchWorkingTimeSlots(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            repository.getWorkingTimeSlots(dayOfWeek).collect { slots ->
                _uiState.update { it.copy(timeSlotsForSelectedDay = slots) }
            }
        }
    }
}
