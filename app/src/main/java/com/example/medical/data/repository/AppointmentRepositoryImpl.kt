package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AppointmentRepositoryImpl : AppointmentRepository {

    private val mockDoctor1 = Doctor(
        id = "d1",
        name = "Dr. Nguyen Van A",
        avatarUrl = null,
        specialty = "Cardiology"
    )

    private val mockDoctor2 = Doctor(
        id = "d2",
        name = "Dr. Tran Thi B",
        avatarUrl = null,
        specialty = "Dermatology"
    )

    private val upcomingAppointments = listOf(
        Appointment(
            id = "a1",
            doctor = mockDoctor1,
            patientName = "Le Van C",
            patientInitial = "L",
            patientGender = "Male",
            patientAge = 30,
            patientIdStr = "P12345",
            patientAvatarUrl = null,
            date = "2026-06-25",
            timeRange = "09:00 - 10:00",
            reason = "Regular checkup",
            location = "Room 101, Building A",
            status = AppointmentStatus.UPCOMING,
            type = AppointmentType.OFFLINE
        ),
        Appointment(
            id = "a2",
            doctor = mockDoctor2,
            patientName = "Le Van C",
            patientInitial = "L",
            patientGender = "Male",
            patientAge = 30,
            patientIdStr = "P12345",
            patientAvatarUrl = null,
            date = "2026-06-26",
            timeRange = "14:00 - 15:00",
            reason = "Skin allergy",
            location = "Online",
            status = AppointmentStatus.UPCOMING,
            type = AppointmentType.ONLINE
        )
    )

    private val historyAppointments = listOf(
        Appointment(
            id = "a3",
            doctor = mockDoctor1,
            patientName = "Le Van C",
            patientInitial = "L",
            patientGender = "Male",
            patientAge = 30,
            patientIdStr = "P12345",
            patientAvatarUrl = null,
            date = "2026-06-01",
            timeRange = "10:00 - 11:00",
            reason = "Heart ache",
            location = "Room 101, Building A",
            status = AppointmentStatus.COMPLETED,
            type = AppointmentType.OFFLINE
        )
    )

    override fun getUpcomingAppointments(): Flow<List<Appointment>> {
        return flowOf(upcomingAppointments)
    }

    override fun getHistoryAppointments(): Flow<List<Appointment>> {
        return flowOf(historyAppointments)
    }
}
