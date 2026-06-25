package com.example.medical.presentation.ui.patient.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.model.BookingDate
import com.example.medical.domain.model.TimeSlot
import com.example.medical.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.example.medical.domain.usecase.appointment.BookAppointmentUseCase

class BookingViewModel(
    private val repository: DoctorRepository,
    private val bookAppointmentUseCase: BookAppointmentUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val doctorId: String = checkNotNull(savedStateHandle["doctorId"])

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getDoctorById(doctorId).collect { doctor ->
                _uiState.update { it.copy(doctor = doctor) }
            }
            repository.getBookingDates().collect { dates ->
                _uiState.update { it.copy(dates = dates, isLoading = false) }
                if (dates.isNotEmpty()) {
                    selectDate(dates.first())
                }
            }
        }
    }

    fun selectDate(date: BookingDate) {
        _uiState.update { it.copy(selectedDate = date, selectedTimeSlot = null) }
        val doctor = _uiState.value.doctor
        if (doctor != null) {
            val scheduleSlots = doctor.workingSchedule[date.dayOfWeekEnum] ?: emptyList()
            val slots = scheduleSlots.mapIndexed { index, slot ->
                TimeSlot(
                    id = index.toString(),
                    timeRange = slot.time,
                    isAvailable = slot.isAvailable
                )
            }
            _uiState.update { it.copy(timeSlots = slots) }
        }
    }

    fun selectTimeSlot(slot: TimeSlot) {
        if (slot.isAvailable) {
            _uiState.update { it.copy(selectedTimeSlot = slot) }
        }
    }

    fun submitBooking(reason: String = "Khám bệnh", type: String = "OFFLINE", onSuccess: () -> Unit) {
        val doctor = _uiState.value.doctor ?: return
        val date = _uiState.value.selectedDate ?: return
        val timeSlot = _uiState.value.selectedTimeSlot ?: return
        
        val dateString = "${date.dateString} ${date.month}"
        val timeString = timeSlot.timeRange.split(" - ").firstOrNull() ?: timeSlot.timeRange

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null, successMessage = null) }
            try {
                bookAppointmentUseCase(
                    doctorId = doctor.id,
                    date = dateString,
                    timeRange = timeString,
                    reason = reason,
                    type = type
                )
                _uiState.update { it.copy(isSubmitting = false, successMessage = "Đặt lịch khám thành công!") }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isSubmitting = false, error = e.message ?: "Đặt lịch thất bại. Vui lòng thử lại!") }
            }
        }
    }
    
    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}
