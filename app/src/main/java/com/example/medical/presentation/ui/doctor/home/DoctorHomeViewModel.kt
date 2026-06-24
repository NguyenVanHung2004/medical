package com.example.medical.presentation.ui.doctor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.doctor.GetDoctorHomeDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
                val today = LocalDate.now()
                val todayStr1 = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val todayStr2 = "${today.dayOfMonth} Tháng ${today.monthValue}"
                
                val filteredAndSortedTodayAppointments = data.todayAppointments
                    .filter { it.date == todayStr1 || it.date == todayStr2 }
                    .sortedBy { it.timeRange.split(" - ").firstOrNull() ?: it.timeRange }
                    
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        doctor = data.doctor,
                        pendingRequests = data.pendingRequests,
                        todayAppointments = filteredAndSortedTodayAppointments
                    )
                }
            }
        }
    }

    fun handleRequestAction(requestId: String, isAccept: Boolean) {
        viewModelScope.launch {
            getDoctorHomeDataUseCase.respondToRequest(requestId, isAccept)
            // Reloading is not needed because the flows will automatically emit new data
        }
    }
}