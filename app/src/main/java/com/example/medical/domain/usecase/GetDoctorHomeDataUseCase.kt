package com.example.medical.domain.usecase

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.DoctorHomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDoctorHomeDataUseCase(
    private val repository: DoctorHomeRepository
) {
    operator fun invoke(): Flow<DoctorHomeData> {
        return combine(
            repository.getDoctorProfile(),
            repository.getPendingRequests(),
            repository.getTodayAppointments()
        ) { doctor, requests, appointments ->
            DoctorHomeData(doctor, requests, appointments)
        }
    }
}

data class DoctorHomeData(
    val doctor: Doctor,
    val pendingRequests: List<AppointmentRequest>,
    val todayAppointments: List<Appointment>
)
