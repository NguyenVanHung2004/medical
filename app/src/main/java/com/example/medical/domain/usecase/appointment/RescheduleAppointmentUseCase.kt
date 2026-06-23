package com.example.medical.domain.usecase.appointment

import com.example.medical.domain.repository.AppointmentRepository

class RescheduleAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointmentId: String) {
        repository.rescheduleAppointment(appointmentId)
    }
}
