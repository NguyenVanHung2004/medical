package com.example.medical.data.repository

import com.example.medical.data.remote.ApiService
import com.example.medical.data.remote.UpdateAppointmentStatusRequest
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.DoctorAppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class DoctorAppointmentRepositoryImpl(
    private val apiService: ApiService
) : DoctorAppointmentRepository {
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
                    location = appt.location ?: appt.doctor.hospital
                )
            }
            emit(pending)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getScheduledAppointments(): Flow<List<Appointment>> = flow {
        try {
            val appointments = apiService.getAppointments()
            val scheduled = appointments.filter { 
                it.status != AppointmentStatus.PENDING && it.status != AppointmentStatus.CANCELLED 
            }
            emit(scheduled)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getAppointmentDetail(id: String): Flow<Appointment?> = flow {
        try {
            val appointments = apiService.getAppointments()
            emit(appointments.find { it.id == id })
        } catch (e: Exception) {
            emit(null)
        }
    }

    override suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit> {
        return try {
            val status = if (accept) "CONFIRMED" else "CANCELLED"
            apiService.updateAppointmentStatus(requestId, UpdateAppointmentStatusRequest(status))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPatientDetail(patientId: String): com.example.medical.domain.model.PatientDetail? {
        // Mock data since API endpoint doesn't exist yet
        return com.example.medical.domain.model.PatientDetail(
            id = patientId,
            fullName = "Nguyễn Văn A",
            avatarUrl = null,
            phone = "0123456789",
            email = "nguyenvana@gmail.com",
            dob = "01/01/1990",
            gender = "Nam",
            address = "123 Đường Xuân Thủy, Cầu Giấy, Hà Nội",
            bloodType = "O+",
            allergies = "Không có"
        )
    }
}
