package com.example.medical.presentation.ui.doctor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.doctor.GetDoctorHomeDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorHomeViewModel(
    private val getDoctorHomeDataUseCase: GetDoctorHomeDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorHomeUIState())
    val uiState: StateFlow<DoctorHomeUIState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getDoctorHomeDataUseCase().collect { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        doctor = data.doctor,
                        pendingRequests = data.pendingRequests,
                        todayAppointments = data.todayAppointments
                    )
                }
            }
        }
    }
}