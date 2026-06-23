package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow

object MockSharedData {
    val pendingRequests = MutableStateFlow(
        listOf(
            AppointmentRequest(
                id = "REQ01",
                patientName = "Trần Thị Bích",
                patientInitial = "B",
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
        )
    )

    val scheduledAppointments = MutableStateFlow(
        listOf(
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
            )
        )
    )

    fun respondToRequest(requestId: String, accept: Boolean) {
        val currentPending = pendingRequests.value
        val requestToMove = currentPending.find { it.id == requestId } ?: return
        
        pendingRequests.value = currentPending.filter { it.id != requestId }
        
        if (accept) {
            val newAppointment = Appointment(
                id = requestToMove.id,
                patientName = requestToMove.patientName,
                patientInitial = requestToMove.patientInitial,
                patientGender = "N/A", 
                patientAge = 0,
                patientIdStr = "N/A", 
                patientAvatarUrl = null,
                date = requestToMove.timeRange.substringBefore(",").trim(), 
                timeRange = requestToMove.timeRange.substringAfter(",").trim(),
                reason = requestToMove.reason,
                location = requestToMove.location,
                status = AppointmentStatus.UPCOMING,
                type = requestToMove.type,
                doctor = Doctor(
                    id = "DOC001",
                    name = "BS. Nguyễn Văn An",
                    avatarUrl = null
                )
            )
            val updatedScheduled = scheduledAppointments.value.toMutableList()
            updatedScheduled.add(0, newAppointment)
            scheduledAppointments.value = updatedScheduled
        }
    }
}
