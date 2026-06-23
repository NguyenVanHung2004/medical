package com.example.medical.presentation.ui.patient.appointment_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.usecase.appointment.GetAppointmentByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentDetailViewModel(
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val appointmentId: String = checkNotNull(savedStateHandle["appointmentId"])
    private val _uiState = MutableStateFlow<Appointment?>(null)
    val uiState: StateFlow<Appointment?> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = getAppointmentByIdUseCase(appointmentId)
        }
    }
}
