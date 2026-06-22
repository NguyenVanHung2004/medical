package com.example.medical.domain.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.Doctor
import kotlinx.coroutines.flow.Flow

interface DoctorHomeRepository {
    fun getDoctorProfile(): Flow<Doctor>
    fun getPendingRequests(): Flow<List<AppointmentRequest>>
    fun getTodayAppointments(): Flow<List<Appointment>>
    suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit>
}
