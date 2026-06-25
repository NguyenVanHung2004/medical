package com.example.medical.domain.usecase.doctor

import com.example.medical.domain.model.PatientDetail
import com.example.medical.domain.repository.DoctorAppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPatientDetailUseCase(private val repository: DoctorAppointmentRepository) {
    operator fun invoke(patientId: String): Flow<PatientDetail?> = flow {
        emit(repository.getPatientDetail(patientId))
    }
}
