package com.example.medical.presentation.ui.booking

import com.example.medical.domain.model.BookingDate
import com.example.medical.domain.model.DoctorDetail
import com.example.medical.domain.model.TimeSlot

data class BookingUiState(
    val isLoading: Boolean = false,
    val doctor: DoctorDetail? = null,
    val dates: List<BookingDate> = emptyList(),
    val timeSlots: List<TimeSlot> = emptyList(),
    val selectedDate: BookingDate? = null,
    val selectedTimeSlot: TimeSlot? = null,
    val error: String? = null
)
