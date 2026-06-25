package com.example.medical.presentation.ui.doctor.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.doctor.GetDoctorAppointmentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import com.example.medical.domain.model.Appointment
import com.example.medical.presentation.ui.common.ToastData
import com.example.medical.presentation.ui.common.ToastType

class DoctorAppointmentViewModel(
    private val getDoctorAppointmentsUseCase: GetDoctorAppointmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorAppointmentUiState())
    val uiState: StateFlow<DoctorAppointmentUiState> = _uiState.asStateFlow()

    private var allScheduledAppointments: List<Appointment> = emptyList()

    init {
        generateAvailableDates()
        loadData()
    }

    private fun generateAvailableDates() {
        val today = LocalDate.now()
        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val dates = (0..6).map { startOfWeek.plusDays(it.toLong()) }
        _uiState.update { it.copy(availableDates = dates, selectedDate = today) }
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val minDelayJob = async { delay(500) }
            getDoctorAppointmentsUseCase().collect { data ->
                allScheduledAppointments = data.scheduledAppointments
                
                val selectedDate = _uiState.value.selectedDate
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val selectedDateStr1 = selectedDate.format(formatter)
                val selectedDateStr2 = "${selectedDate.dayOfMonth} Tháng ${selectedDate.monthValue}"
                
                val filteredAndSorted = allScheduledAppointments
                    .filter { it.date == selectedDateStr1 || it.date == selectedDateStr2 }
                    .sortedBy { it.timeRange.split(" - ").firstOrNull() ?: it.timeRange }

                minDelayJob.await()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pendingRequests = data.pendingRequests,
                        scheduledAppointments = filteredAndSorted
                    )
                }
            }
        }
    }

    private fun updateFilteredAppointments() {
        val selectedDate = _uiState.value.selectedDate
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val selectedDateStr1 = selectedDate.format(formatter)
        val selectedDateStr2 = "${selectedDate.dayOfMonth} Tháng ${selectedDate.monthValue}"
        
        val filteredAndSorted = allScheduledAppointments
            .filter { it.date == selectedDateStr1 || it.date == selectedDateStr2 }
            .sortedBy { it.timeRange.split(" - ").firstOrNull() ?: it.timeRange }

        _uiState.update {
            it.copy(scheduledAppointments = filteredAndSorted)
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        updateFilteredAppointments()
    }

    fun handleRequestAction(requestId: String, isAccept: Boolean) {
        viewModelScope.launch {
            getDoctorAppointmentsUseCase.respondToRequest(requestId, isAccept).onSuccess {
                loadData()
                val message = if (isAccept) "Đã chấp nhận yêu cầu" else "Đã từ chối yêu cầu"
                showToast(message, ToastType.SUCCESS)
            }
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
