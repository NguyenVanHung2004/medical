package com.example.medical.domain.usecase.appointment

import com.example.medical.domain.repository.AppointmentRepository

class BookAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(doctorName: String, avatarUrl: String?, specialty: String, date: String, timeRange: String) {
        repository.bookAppointment(doctorName, avatarUrl, specialty, date, timeRange)
    }
}
