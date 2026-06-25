package com.example.medical.presentation.ui.patient.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.appointment.CancelAppointmentUseCase
import com.example.medical.domain.usecase.appointment.GetHistoryAppointmentsUseCase
import com.example.medical.domain.usecase.appointment.GetUpcomingAppointmentsUseCase
import com.example.medical.domain.usecase.appointment.RescheduleAppointmentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppointmentsViewModel(
    private val getUpcomingAppointmentsUseCase: GetUpcomingAppointmentsUseCase,
    private val getHistoryAppointmentsUseCase: GetHistoryAppointmentsUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val rescheduleAppointmentUseCase: RescheduleAppointmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentsUiState())
    val uiState: StateFlow<AppointmentsUiState> = _uiState.asStateFlow()

    init {
        loadAppointments()
    }

    fun loadAppointments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            launch {
                getUpcomingAppointmentsUseCase().collect { upcoming ->
                    _uiState.update { it.copy(upcomingAppointments = upcoming, isLoading = false) }
                }
            }
            
            launch {
                getHistoryAppointmentsUseCase().collect { history ->
                    _uiState.update { it.copy(historyAppointments = history) }
                }
            }
        }
    }

    fun requestCancelAppointment(appointmentId: String) {
        _uiState.update { it.copy(appointmentToCancel = appointmentId) }
    }

    fun hideCancelDialog() {
        _uiState.update { it.copy(appointmentToCancel = null) }
    }

    fun confirmCancelAppointment() {
        val appointmentId = _uiState.value.appointmentToCancel ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, appointmentToCancel = null, error = null, successMessage = null) }
            try {
                cancelAppointmentUseCase(appointmentId)
                _uiState.update { it.copy(isLoading = false, successMessage = "Đã hủy lịch hẹn") }
                loadAppointments()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Không thể hủy lịch hẹn") }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }

    fun rescheduleAppointment(appointmentId: String) {
        viewModelScope.launch {
            rescheduleAppointmentUseCase(appointmentId)
            loadAppointments()
        }
    }
}
