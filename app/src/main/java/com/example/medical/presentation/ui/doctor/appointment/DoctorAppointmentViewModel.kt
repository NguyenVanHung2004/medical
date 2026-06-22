package com.example.medical.presentation.ui.doctor.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.GetDoctorAppointmentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorAppointmentViewModel(
    private val getDoctorAppointmentsUseCase: GetDoctorAppointmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorAppointmentUiState())
    val uiState: StateFlow<DoctorAppointmentUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getDoctorAppointmentsUseCase().collect { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pendingRequests = data.pendingRequests,
                        scheduledAppointments = data.scheduledAppointments
                    )
                }
            }
        }
    }
}
