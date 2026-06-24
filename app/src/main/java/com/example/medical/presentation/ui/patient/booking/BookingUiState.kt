package com.example.medical.presentation.ui.patient.booking

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
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)
