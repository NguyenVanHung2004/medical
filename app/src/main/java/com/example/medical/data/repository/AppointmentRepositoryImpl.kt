package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AppointmentRepositoryImpl : AppointmentRepository {
    override fun getUpcomingAppointments(): Flow<List<Appointment>> {


        return TODO("Provide the return value")
    }

    override fun getHistoryAppointments(): Flow<List<Appointment>> {
        return TODO("Provide the return value")

    }

}
