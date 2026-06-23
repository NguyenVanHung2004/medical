package com.example.medical.presentation.ui.doctor.profile

import com.example.medical.domain.model.Doctor
import com.example.medical.domain.model.WorkingTimeSlot
import java.time.DayOfWeek

data class DoctorProfileUiState(
    val isLoading: Boolean = true,
    val doctor: Doctor? = null,
    val errorMessage: String? = null,
    val isWorkingHoursDialogVisible: Boolean = false,
    val isEditProfileDialogVisible: Boolean = false,
    val isEditFeesDialogVisible: Boolean = false,
    val selectedDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val timeSlotsForSelectedDay: List<WorkingTimeSlot> = emptyList(),
    val weeklySchedule: Map<DayOfWeek, List<WorkingTimeSlot>> = emptyMap()
)
