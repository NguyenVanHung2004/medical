package com.example.medical.domain.usecase.doctor

import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.DoctorProfileRepository
import kotlinx.coroutines.flow.Flow

class GetDoctorProfileUseCase(private val repository: DoctorProfileRepository) {
    operator fun invoke(): Flow<Doctor> {
        return repository.getDoctorProfile()
    }
}
