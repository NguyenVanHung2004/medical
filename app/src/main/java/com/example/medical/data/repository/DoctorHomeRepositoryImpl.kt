package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.repository.DoctorHomeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.medical.domain.model.Doctor

class DoctorHomeRepositoryImpl : DoctorHomeRepository {
    override fun getDoctorProfile(): Flow<Doctor> = flow {
        delay(300) // Mock network delay
        emit(Doctor(id = "DOC001", name = "BS. Nguyễn Văn An", avatarUrl = null))
    }

    override fun getPendingRequests(): Flow<List<AppointmentRequest>> = MockSharedData.pendingRequests

    override fun getTodayAppointments(): Flow<List<Appointment>> = flow {
        MockSharedData.scheduledAppointments.collect { list ->
            // Assume today's appointments are ones with date == "Ngày mai" for mock purposes
            emit(list.filter { it.date == "Ngày mai" || it.date.contains("Hôm nay", ignoreCase = true) })
        }
    }

    override suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit> {
        delay(200)
        MockSharedData.respondToRequest(requestId, accept)
        return Result.success(Unit)
    }
}
