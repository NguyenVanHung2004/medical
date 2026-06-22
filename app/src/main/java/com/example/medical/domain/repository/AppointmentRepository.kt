package com.example.medical.domain.repository

import com.example.medical.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getUpcomingAppointments(): Flow<List<Appointment>>
    fun getHistoryAppointments(): Flow<List<Appointment>>
}
