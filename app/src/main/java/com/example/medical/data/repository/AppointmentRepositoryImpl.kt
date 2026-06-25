package com.example.medical.data.repository

import com.example.medical.data.remote.ApiService
import com.example.medical.data.remote.BookAppointmentRequest
import com.example.medical.data.remote.UpdateAppointmentStatusRequest
import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AppointmentRepositoryImpl(
    private val apiService: ApiService
) : AppointmentRepository {

    override fun getUpcomingAppointments(): Flow<List<Appointment>> = flow {
        try {
            val appointments = apiService.getAppointments()
            emit(appointments.filter { 
                it.status == AppointmentStatus.UPCOMING || 
                it.status == AppointmentStatus.CONFIRMED ||
                it.status == AppointmentStatus.HAPPENING || 
                it.status == AppointmentStatus.PENDING 
            })
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getHistoryAppointments(): Flow<List<Appointment>> = flow {
        try {
            val appointments = apiService.getAppointments()
            emit(appointments.filter { 
                it.status == AppointmentStatus.COMPLETED || 
                it.status == AppointmentStatus.CANCELLED 
            })
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun cancelAppointment(appointmentId: String) {
        try {
            apiService.updateAppointmentStatus(appointmentId, UpdateAppointmentStatusRequest("CANCELLED"))
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun bookAppointment(doctorId: String, date: String, timeRange: String, reason: String, type: String) {
        try {
            apiService.bookAppointment(
                BookAppointmentRequest(
                    doctorId = doctorId,
                    date = date,
                    timeRange = timeRange,
                    reason = reason,
                    type = type
                )
            )
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun rescheduleAppointment(appointmentId: String) {
        // Mocked or missing in API right now, could be a combination of update + change time, or cancel and book new.
        // For now, let's just mark it as PENDING and change time locally if API supports it, 
        // but NextJS API doesn't have a direct reschedule route yet.
    }

    override suspend fun getAppointmentById(id: String): Appointment? {
        return try {
            // For now, we can get all and filter, or API needs a specific endpoint.
            // Let's get all and find it since API doesn't have getAppointmentById yet.
            val appointments = apiService.getAppointments()
            appointments.find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }
}
