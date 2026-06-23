package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _upcomingAppointments = MutableStateFlow(listOf(
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
    ))

    private val _historyAppointments = MutableStateFlow(listOf(
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
    ))

    override fun getUpcomingAppointments(): Flow<List<Appointment>> {
        return _upcomingAppointments.asStateFlow()
    }

    override fun getHistoryAppointments(): Flow<List<Appointment>> {
        return _historyAppointments.asStateFlow()
    }

    override suspend fun cancelAppointment(appointmentId: String) {
        val currentList = _upcomingAppointments.value
        _upcomingAppointments.value = currentList.filter { it.id != appointmentId }
    }

    override suspend fun bookAppointment(doctorName: String, avatarUrl: String?, specialty: String, date: String, timeRange: String) {
        val newAppt = Appointment(
            id = "a_mock_${System.currentTimeMillis()}",
            doctor = Doctor(
                id = "d_mock_1",
                name = doctorName,
                avatarUrl = avatarUrl,
                specialty = specialty
            ),
            patientName = "Mock Patient",
            patientInitial = "M",
            patientGender = "Male",
            patientAge = 30,
            patientIdStr = "P12345",
            patientAvatarUrl = null,
            date = date,
            timeRange = timeRange,
            reason = "Khám tổng quát",
            location = "Phòng khám số 12, Tầng 3",
            status = AppointmentStatus.UPCOMING,
            type = AppointmentType.OFFLINE
        )
        _upcomingAppointments.value = listOf(newAppt) + _upcomingAppointments.value
    }

    override suspend fun rescheduleAppointment(appointmentId: String) {
        val appt = _historyAppointments.value.find { it.id == appointmentId } 
            ?: _upcomingAppointments.value.find { it.id == appointmentId }
        
        if (appt != null) {
            val rescheduledAppt = appt.copy(
                id = "a_mock_${System.currentTimeMillis()}",
                date = "2026-07-01",
                timeRange = "10:00 - 11:00",
                status = AppointmentStatus.UPCOMING
            )
            _upcomingAppointments.value = listOf(rescheduledAppt) + _upcomingAppointments.value
        }
    }

    override suspend fun getAppointmentById(id: String): Appointment? {
        return _upcomingAppointments.value.find { it.id == id }
            ?: _historyAppointments.value.find { it.id == id }
    }
}
