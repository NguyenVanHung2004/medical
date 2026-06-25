package com.example.medical.domain.usecase.appointment

import com.example.medical.domain.repository.AppointmentRepository

class BookAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(doctorId: String, date: String, timeRange: String, reason: String, type: String) {
        repository.bookAppointment(doctorId, date, timeRange, reason, type)
    }
}
