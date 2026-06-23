package com.example.medical.domain.usecase.appointment

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow

class GetUpcomingAppointmentsUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(): Flow<List<Appointment>> {
        return repository.getUpcomingAppointments()
    }
}
