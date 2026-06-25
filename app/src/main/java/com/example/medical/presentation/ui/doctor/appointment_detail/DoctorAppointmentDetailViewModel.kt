package com.example.medical.presentation.ui.doctor.appointment_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.doctor.GetDoctorAppointmentDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class DoctorAppointmentDetailViewModel(
    private val getAppointmentDetailUseCase: GetDoctorAppointmentDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorAppointmentDetailUiState())
    val uiState: StateFlow<DoctorAppointmentDetailUiState> = _uiState.asStateFlow()

    fun loadAppointmentDetail(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val minDelayJob = async { delay(370) }
            getAppointmentDetailUseCase(id).collect { appointment ->
                minDelayJob.await()
                if (appointment != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            appointment = appointment
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Appointment not found"
                        )
                    }
                }
            }
        }
    }
}
