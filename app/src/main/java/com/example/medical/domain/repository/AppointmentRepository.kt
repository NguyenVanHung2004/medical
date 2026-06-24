package com.example.medical.domain.repository

import com.example.medical.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getUpcomingAppointments(): Flow<List<Appointment>>
    fun getHistoryAppointments(): Flow<List<Appointment>>
    suspend fun getAppointmentById(id: String): Appointment?
    suspend fun cancelAppointment(appointmentId: String)
    suspend fun bookAppointment(doctorId: String, date: String, timeRange: String, reason: String, type: String)
    suspend fun rescheduleAppointment(appointmentId: String)
}
