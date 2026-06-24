package com.example.medical.data.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.AppointmentRequest
import com.example.medical.domain.model.AppointmentStatus
import com.example.medical.domain.model.ConsultationType
import com.example.medical.domain.model.Doctor
import com.example.medical.domain.model.Notification
import com.example.medical.domain.model.NotificationType
import com.example.medical.domain.model.User
import com.example.medical.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object MockSharedData {
    var mockPatient = User(
        id = "u1", 
        email = "test@gmail.com", 
        phone = null, 
        fullName = "Nguyễn Văn A", 
        avatarUrl = null, 
        role = UserRole.PATIENT, 
        token = "fake_token_xyz"
    )

    val doctorProfile = MutableStateFlow(
        Doctor(
            id = "DOC001",
            name = "BS. Nguyễn Văn An",
            avatarUrl = null,
            specialty = "Chuyên khoa Tim mạch",
            hospital = "Bệnh viện Đại học Y Hà Nội",
            experience = "15 năm kinh nghiệm",
            isOnlineConsultationEnabled = true,
            onlineConsultationFee = 200000,
            isInPersonConsultationEnabled = true,
            inPersonConsultationFee = 300000,
            workingHoursSummary = "Tuỳ chỉnh theo ngày"
        )
    )

    val mockDoctor2 = Doctor(
        id = "DOC002",
        name = "BS. Phạm Văn A",
        avatarUrl = "https://i.pravatar.cc/150?img=11",
        specialty = "Nhi khoa"
    )
    
    val doctorsList = listOf(doctorProfile.value, mockDoctor2)

    private val today = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val appointmentsList = MutableStateFlow(
        listOf(
            Appointment(
                id = "APP01",
                patientName = mockPatient.fullName,
                patientInitial = "N",
                patientGender = "Nam",
                patientAge = 35,
                patientIdStr = mockPatient.id,
                patientAvatarUrl = mockPatient.avatarUrl,
                date = today.format(dateFormatter),
                timeRange = "09:00 - 09:30",
                reason = "Tái khám định kỳ",
                location = "Phòng 204, Khu A",
                status = AppointmentStatus.HAPPENING,
                type = ConsultationType.ONLINE,
                doctor = doctorProfile.value
            ),
            Appointment(
                id = "APP02",
                patientName = mockPatient.fullName,
                patientInitial = "N",
                patientGender = "Nam",
                patientAge = 35,
                patientIdStr = mockPatient.id,
                patientAvatarUrl = mockPatient.avatarUrl,
                date = today.plusDays(1).format(dateFormatter),
                timeRange = "10:00 - 10:30",
                reason = "Khám tổng quát",
                location = "Phòng khám số 12, Tầng 3",
                status = AppointmentStatus.UPCOMING,
                type = ConsultationType.OFFLINE,
                doctor = mockDoctor2
            ),
            Appointment(
                id = "APP03",
                patientName = "Trần Thị Bích",
                patientInitial = "B",
                patientGender = "Nữ",
                patientAge = 28,
                patientIdStr = "u2",
                patientAvatarUrl = null,
                date = today.plusDays(2).format(dateFormatter),
                timeRange = "09:00 - 09:30",
                reason = "Tái khám sau phẫu thuật",
                location = null,
                status = AppointmentStatus.PENDING,
                type = ConsultationType.ONLINE,
                doctor = doctorProfile.value
            )
        )
    )

    val notificationsList = MutableStateFlow(
        listOf(
            Notification(
                id = "1",
                title = "Nhắc nhở lịch hẹn",
                message = "Bạn có lịch hẹn với ${doctorProfile.value.name} lúc 09:00 sáng mai.",
                timeAgo = "2 giờ trước",
                isUnread = true,
                type = NotificationType.REMINDER
            )
        )
    )

    // Helper cho Doctor repository
    val pendingRequests = appointmentsList.map { list ->
        list.filter { it.status == AppointmentStatus.PENDING }.map { appt ->
            AppointmentRequest(
                id = appt.id,
                patientName = appt.patientName,
                patientInitial = appt.patientInitial,
                timeRange = "${appt.date}, ${appt.timeRange}",
                reason = appt.reason,
                type = appt.type,
                location = appt.location
            )
        }
    }

    val scheduledAppointments = appointmentsList.map { list ->
        list.filter { it.status != AppointmentStatus.PENDING && it.status != AppointmentStatus.CANCELLED }
    }

    fun respondToRequest(requestId: String, accept: Boolean) {
        val currentList = appointmentsList.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val appt = currentList[index]
            val newStatus = if (accept) AppointmentStatus.UPCOMING else AppointmentStatus.CANCELLED
            currentList[index] = appt.copy(status = newStatus)
            appointmentsList.value = currentList
        }
    }
}
