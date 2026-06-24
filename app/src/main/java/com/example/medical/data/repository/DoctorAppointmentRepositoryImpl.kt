package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.DoctorAppointmentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DoctorAppointmentRepositoryImpl : DoctorAppointmentRepository {
    override fun getPendingRequests(): Flow<List<AppointmentRequest>> = MockSharedData.pendingRequests

    override fun getScheduledAppointments(): Flow<List<Appointment>> = MockSharedData.scheduledAppointments

    override fun getAppointmentDetail(id: String): Flow<Appointment?> = flow {
        delay(300)
        emit(MockSharedData.appointmentsList.value.find { it.id == id })
    }

    override suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit> {
        delay(200)
        MockSharedData.respondToRequest(requestId, accept)
        return Result.success(Unit)
    }
}
