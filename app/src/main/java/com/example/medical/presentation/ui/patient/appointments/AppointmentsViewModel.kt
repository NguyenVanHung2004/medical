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

    private fun loadAppointments() {
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

    fun cancelAppointment(appointmentId: String) {
        viewModelScope.launch {
            cancelAppointmentUseCase(appointmentId)
        }
    }

    fun rescheduleAppointment(appointmentId: String) {
        viewModelScope.launch {
            rescheduleAppointmentUseCase(appointmentId)
        }
    }
}
