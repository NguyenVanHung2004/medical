package com.example.medical.presentation.ui.doctor.patient_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.doctor.GetPatientDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class PatientDetailViewModel(
    private val getPatientDetailUseCase: GetPatientDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientDetailUiState())
    val uiState: StateFlow<PatientDetailUiState> = _uiState.asStateFlow()

    fun loadPatientDetail(patientId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val minDelayJob = async { delay(500) }
            getPatientDetailUseCase(patientId).collect { detail ->
                minDelayJob.await()
                if (detail != null) {
                    _uiState.update { it.copy(isLoading = false, patientDetail = detail) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Không tìm thấy thông tin bệnh nhân") }
                }
            }
        }
    }
}
