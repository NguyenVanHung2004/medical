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
    override fun getPendingRequests(): Flow<List<AppointmentRequest>> = flow {
        delay(400)
        emit(listOf(
            AppointmentRequest(
                id = "REQ01",
                patientName = "Trần Thị Bích",
                patientInitial = "T",
                timeRange = "Ngày mai, 09:00 - 09:30",
                reason = "Tái khám sau phẫu thuật",
                type = AppointmentType.ONLINE
            ),
            AppointmentRequest(
                id = "REQ02",
                patientName = "Lê Văn Cường",
                patientInitial = "L",
                timeRange = "15/06/2026, 14:00 - 14:30",
                reason = "Tư vấn kết quả xét nghiệm",
                type = AppointmentType.OFFLINE,
                location = "Phòng 204, Khu A"
            ),
            AppointmentRequest(
                id = "REQ03",
                patientName = "Phạm Minh Đức",
                patientInitial = "P",
                timeRange = "16/06/2026, 10:00 - 10:30",
                reason = "Khám tổng quát",
                type = AppointmentType.ONLINE
            )
        ))
    }

    override fun getScheduledAppointments(): Flow<List<Appointment>> = flow {
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
                timeRange = "09:00 - 09:30",
                reason = "Tái khám định kỳ",
                location = null,
                status = AppointmentStatus.HAPPENING,
                type = AppointmentType.ONLINE,
                doctor = Doctor(
                    id = "DOC001",
                    name = "BS. Nguyễn Văn An",
                    avatarUrl = null
                )
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
                timeRange = "10:00 - 10:30",
                reason = "Khám tổng quát",
                location = "Phòng 204, Khu A",
                status = AppointmentStatus.UPCOMING,
                type = AppointmentType.OFFLINE,
                doctor = Doctor(
                    id = "DOC001",
                    name = "BS. Nguyễn Văn An",
                    avatarUrl = null
                )
            ),
            Appointment(
                id = "APP03",
                patientName = "Lê Văn Cường",
                patientInitial = "C",
                patientGender = "nam",
                patientAge = 32,
                patientIdStr = "198275",
                patientAvatarUrl = null,
                date = "15/06/2026",
                timeRange = "14:00 - 14:30",
                reason = "Tư vấn kết quả xét nghiệm",
                location = null,
                status = AppointmentStatus.UPCOMING,
                type = AppointmentType.ONLINE,
                doctor = Doctor(
                    id = "DOC001",
                    name = "BS. Nguyễn Văn An",
                    avatarUrl = null
                )
            )
        ))
    }

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
}
