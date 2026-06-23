package com.example.medical.domain.usecase.doctor

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.repository.DoctorAppointmentRepository
import kotlinx.coroutines.flow.Flow

class GetDoctorAppointmentDetailUseCase(
    private val repository: DoctorAppointmentRepository
) {
    operator fun invoke(id: String): Flow<Appointment?> {
        return repository.getAppointmentDetail(id)
    }
}
