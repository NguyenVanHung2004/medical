package com.example.medical.domain.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import kotlinx.coroutines.flow.Flow

interface DoctorAppointmentRepository {
    fun getPendingRequests(): Flow<List<AppointmentRequest>>
    fun getScheduledAppointments(): Flow<List<Appointment>>
}
