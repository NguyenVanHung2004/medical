package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.DoctorAppointmentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DoctorAppointmentRepositoryImpl : DoctorAppointmentRepository {
    override fun getPendingRequests(): Flow<List<AppointmentRequest>> = MockSharedData.pendingRequests

    override fun getScheduledAppointments(): Flow<List<Appointment>> = MockSharedData.scheduledAppointments

    override fun getAppointmentDetail(id: String): Flow<Appointment?> = flow {
        delay(300)
        // Just return a mock appointment for now based on the id
        emit(
            Appointment(
                id = id,
                patientName = "Trần Thị Bích",
                patientInitial = "T",
                patientGender = "Nữ",
                patientAge = 35,
                patientIdStr = "123456",
                patientAvatarUrl = null,
                date = "Ngày mai",
                timeRange = "09:00 - 09:30",
                reason = "Tái khám sau phẫu thuật. Bệnh nhân thỉnh thoảng thấy nhức nhẹ ở vết mổ.",
                location = null,
                status = AppointmentStatus.HAPPENING,
                type = AppointmentType.ONLINE,
                doctor = Doctor(
                    id = "DOC001",
                    name = "BS. Nguyễn Văn An",
                    avatarUrl = null
                )
            )
        )
    }

    override suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit> {
        delay(200)
        MockSharedData.respondToRequest(requestId, accept)
        return Result.success(Unit)
    }
}
