package com.example.medical.domain.usecase.doctor

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.repository.DoctorAppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDoctorAppointmentsUseCase(
    private val repository: DoctorAppointmentRepository
) {
    operator fun invoke(): Flow<DoctorAppointmentsData> {
        return combine(
            repository.getPendingRequests(),
            repository.getScheduledAppointments()
        ) { requests, appointments ->
            DoctorAppointmentsData(requests, appointments)
        }
    }

    suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit> {
        return repository.respondToRequest(requestId, accept)
    }
}

data class DoctorAppointmentsData(
    val pendingRequests: List<AppointmentRequest>,
    val scheduledAppointments: List<Appointment>
)
