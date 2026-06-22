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

class BookingViewModel(
    private val repository: DoctorRepository,
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
        viewModelScope.launch {
            repository.getTimeSlots(date.dateString).collect { slots ->
                _uiState.update { it.copy(timeSlots = slots) }
            }
        }
    }

    fun selectTimeSlot(slot: TimeSlot) {
        if (slot.isAvailable) {
            _uiState.update { it.copy(selectedTimeSlot = slot) }
        }
    }
}
