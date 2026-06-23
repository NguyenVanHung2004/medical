package com.example.medical.presentation.ui.patient.appointment_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.usecase.appointment.GetAppointmentByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.medical.domain.usecase.appointment.CancelAppointmentUseCase

class AppointmentDetailViewModel(
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val appointmentId: String = checkNotNull(savedStateHandle["appointmentId"])
    private val _uiState = MutableStateFlow(AppointmentDetailUiState(isLoading = true))
    val uiState: StateFlow<AppointmentDetailUiState> = _uiState.asStateFlow()

    init {
        loadAppointment()
    }

    private fun loadAppointment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val appointment = getAppointmentByIdUseCase(appointmentId)
            _uiState.update { it.copy(appointment = appointment, isLoading = false) }
        }
    }

    fun showCancelDialog() {
        _uiState.update { it.copy(showCancelDialog = true) }
    }

    fun hideCancelDialog() {
        _uiState.update { it.copy(showCancelDialog = false) }
    }

    fun cancelAppointment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showCancelDialog = false) }
            try {
                cancelAppointmentUseCase(appointmentId)
                _uiState.update { it.copy(isCancelled = true, isLoading = false) }
                // Reload to get updated status
                loadAppointment()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
