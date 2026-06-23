package com.example.medical.domain.usecase.appointment

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.repository.AppointmentRepository

class GetAppointmentByIdUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(id: String): Appointment? {
        return repository.getAppointmentById(id)
    }
}
