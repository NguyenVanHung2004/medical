package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.repository.DoctorHomeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DoctorHomeRepositoryImpl : DoctorHomeRepository {
    override fun getDoctorProfile(): Flow<Doctor> = flow {
        delay(300) // Mock network delay
        emit(Doctor(id = "DOC001", name = "BS. Nguyễn Văn An", avatarUrl = null))
    }

    override fun getPendingRequests(): Flow<List<AppointmentRequest>> = flow {
        delay(400)
        emit(listOf(
            AppointmentRequest(
                id = "REQ01",
                patientName = "Trần Thị Bích",
                patientInitial = "B",
                timeRange = "09:00 - 09:30",
                reason = "Tái khám sau phẫu thuật",
                type = AppointmentType.ONLINE
            ),
            AppointmentRequest(
                id = "REQ02",
                patientName = "Lê Văn Cường",
                patientInitial = "L",
                timeRange = "10:30 - 11:00",
                reason = "Tư vấn kết quả xét nghiệm",
                type = AppointmentType.OFFLINE,
                location = "Phòng 204, Khu A"
            )
        ))
    }

    override fun getTodayAppointments(): Flow<List<Appointment>> = flow {
        delay(500)
        emit(listOf(
            Appointment(
                id = "APP01",
                patientName = "Nguyễn Thị Mai",
                patientInitial = "M",
                patientGender = "nữ",
                patientAge = 65,
                patientIdStr = "198273",
                patientAvatarUrl = null,
                date = "Ngày mai",
                timeRange = "10:00 - 10:30",
                reason = "Khám định kỳ",
                location = null,
                status = AppointmentStatus.HAPPENING,
                type = AppointmentType.ONLINE
            ),
            Appointment(
                id = "APP02",
                patientName = "Phạm Văn Hùng",
                patientInitial = "H",
                patientGender = "nam",
                patientAge = 45,
                patientIdStr = "198274",
                patientAvatarUrl = null,
                date = "16/06/2026",
                timeRange = "11:00 - 11:30",
                reason = "Tái khám nội tiết",
                location = "Phòng 204, Khu A",
                status = AppointmentStatus.UPCOMING,
                type = AppointmentType.OFFLINE
            )
        ))
    }

    override suspend fun respondToRequest(requestId: String, accept: Boolean): Result<Unit> {
        delay(200)
        return Result.success(Unit)
    }
}
