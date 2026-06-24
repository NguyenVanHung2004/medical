package com.example.medical.data.repository

import com.example.medical.data.remote.ApiService
import com.example.medical.data.remote.UpdateAppointmentStatusRequest
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.repository.DoctorHomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.medical.domain.model.Doctor
import java.lang.Exception
import java.time.LocalDate

class DoctorHomeRepositoryImpl(
    private val apiService: ApiService
) : DoctorHomeRepository {
    override fun getDoctorProfile(): Flow<Doctor> = flow {
        try {
            val user = apiService.getProfile()
            emit(Doctor(id = user.id, name = user.fullName, avatarUrl = user.avatarUrl))
        } catch (e: Exception) {
            emit(Doctor(id = "DOC001", name = "BS. Nguyễn Văn An", avatarUrl = null))
        }
    }

    override fun getPendingRequests(): Flow<List<AppointmentRequest>> = flow {
        try {
            val appointments = apiService.getAppointments()
            val pending = appointments.filter { it.status == AppointmentStatus.PENDING }.map { appt ->
                AppointmentRequest(
                    id = appt.id,
                    patientName = appt.patientName,
                    patientInitial = appt.patientInitial,
                    timeRange = "${appt.date}, ${appt.timeRange}",
                    reason = appt.reason,
                    type = appt.type,
                    location = appt.location
                )
            }
            emit(pending)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getTodayAppointments(): Flow<List<Appointment>> = flow {
        try {
            val appointments = apiService.getAppointments()
            // We can just filter HAPPENING, UPCOMING or compare date.
            // For now, let's filter UPCOMING and HAPPENING
            val today = appointments.filter { 
                it.status == AppointmentStatus.UPCOMING || it.status == AppointmentStatus.HAPPENING 
            }
            emit(today)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit> {
        return try {
            val status = if (accept) "UPCOMING" else "CANCELLED"
            apiService.updateAppointmentStatus(requestId, UpdateAppointmentStatusRequest(status))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
