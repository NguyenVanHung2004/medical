package com.example.medical.presentation.ui.doctor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.doctor.GetDoctorHomeDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.medical.presentation.ui.common.ToastData
import com.example.medical.presentation.ui.common.ToastType

class DoctorHomeViewModel(
    private val getDoctorHomeDataUseCase: GetDoctorHomeDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorHomeUIState())
    val uiState: StateFlow<DoctorHomeUIState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val minDelayJob = async { delay(500) }
            getDoctorHomeDataUseCase().collect { data ->
                val today = LocalDate.now()
                val todayStr1 = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val todayStr2 = "${today.dayOfMonth} Tháng ${today.monthValue}"
                
                val filteredAndSortedTodayAppointments = data.todayAppointments
                    .filter { it.date == todayStr1 || it.date == todayStr2 }
                    .sortedBy { it.timeRange.split(" - ").firstOrNull() ?: it.timeRange }
                    
                minDelayJob.await()
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
            loadData()
            val message = if (isAccept) "Đã chấp nhận yêu cầu" else "Đã từ chối yêu cầu"
            showToast(message, ToastType.SUCCESS)
        }
    }
    
    private fun showToast(message: String, type: ToastType) {
        _uiState.update { it.copy(toastData = ToastData(message, type)) }
        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            _uiState.update { it.copy(toastData = null) }
        }
    }
}