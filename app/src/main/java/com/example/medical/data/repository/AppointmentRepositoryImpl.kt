package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentStatus

import com.example.medical.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppointmentRepositoryImpl : AppointmentRepository {

    override fun getUpcomingAppointments(): Flow<List<Appointment>> {
        return MockSharedData.appointmentsList.map { list ->
            list.filter { it.patientIdStr == MockSharedData.mockPatient.id && (it.status == AppointmentStatus.UPCOMING || it.status == AppointmentStatus.HAPPENING || it.status == AppointmentStatus.PENDING) }
        }
    }

    override fun getHistoryAppointments(): Flow<List<Appointment>> {
        return MockSharedData.appointmentsList.map { list ->
            list.filter { it.patientIdStr == MockSharedData.mockPatient.id && (it.status == AppointmentStatus.COMPLETED || it.status == AppointmentStatus.CANCELLED) }
        }
    }

    override suspend fun cancelAppointment(appointmentId: String) {
        val currentList = MockSharedData.appointmentsList.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == appointmentId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(status = AppointmentStatus.CANCELLED)
            MockSharedData.appointmentsList.value = currentList
        }
    }

    override suspend fun bookAppointment(doctorName: String, avatarUrl: String?, specialty: String, date: String, timeRange: String) {
        val doctor = MockSharedData.doctorsList.find { it.name == doctorName } ?: MockSharedData.mockDoctor2
        val newAppt = Appointment(
            id = "APP_${System.currentTimeMillis()}",
            doctor = doctor,
            patientName = MockSharedData.mockPatient.fullName,
            patientInitial = MockSharedData.mockPatient.fullName.firstOrNull()?.toString() ?: "N",
            patientGender = "Nam",
            patientAge = 35,
            patientIdStr = MockSharedData.mockPatient.id,
            patientAvatarUrl = MockSharedData.mockPatient.avatarUrl,
            date = date,
            timeRange = timeRange,
            reason = "Khám tổng quát",
            location = "Phòng khám chính",
            status = AppointmentStatus.PENDING,
            type = com.example.medical.domain.model.ConsultationType.OFFLINE
        )
        val currentList = MockSharedData.appointmentsList.value.toMutableList()
        currentList.add(0, newAppt)
        MockSharedData.appointmentsList.value = currentList
    }

    override suspend fun rescheduleAppointment(appointmentId: String) {
        val currentList = MockSharedData.appointmentsList.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == appointmentId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(
                date = "2026-07-01",
                timeRange = "10:00 - 11:00",
                status = AppointmentStatus.PENDING // Needs approval again
            )
            MockSharedData.appointmentsList.value = currentList
        }
    }

    override suspend fun getAppointmentById(id: String): Appointment? {
        return MockSharedData.appointmentsList.value.find { it.id == id }
    }
}
