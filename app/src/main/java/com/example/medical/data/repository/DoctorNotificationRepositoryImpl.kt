package com.example.medical.data.repository

import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.DoctorNotification
import com.example.medical.domain.model.NotificationType
import com.example.medical.domain.repository.DoctorNotificationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class DoctorNotificationRepositoryImpl : DoctorNotificationRepository {

    private val _notifications = MutableStateFlow<List<DoctorNotification>>(emptyList())

    init {
        // Mock data
        _notifications.value = listOf(
            DoctorNotification(
                id = UUID.randomUUID().toString(),
                type = NotificationType.NEW_APPOINTMENT_REQUEST,
                patientName = "Trần Thị Bích",
                timeInfo = "lúc 09:00 ngày mai",
                timestamp = "10 phút trước",
                isRead = false
            ),
            DoctorNotification(
                id = UUID.randomUUID().toString(),
                type = NotificationType.APPOINTMENT_CANCELLED,
                patientName = "Nguyễn Văn A",
                timeInfo = "lúc 14:00 chiều nay",
                timestamp = "2 giờ trước",
                isRead = true
            ),
            DoctorNotification(
                id = UUID.randomUUID().toString(),
                type = NotificationType.UPCOMING_APPOINTMENT,
                patientName = "Phạm Văn Hùng",
                timeInfo = "sau 15 phút nữa",
                timestamp = "Hôm qua, 08:30",
                isRead = true,
                appointmentType = AppointmentType.ONLINE
            )
        )
    }

    override fun getNotifications(): Flow<List<DoctorNotification>> = _notifications.asStateFlow()

    override suspend fun markAsRead(notificationId: String) {
        delay(300)
        _notifications.update { currentList ->
            currentList.map {
                if (it.id == notificationId) it.copy(isRead = true) else it
            }
        }
    }

    override suspend fun markAllAsRead() {
        delay(300)
        _notifications.update { currentList ->
            currentList.map { it.copy(isRead = true) }
        }
    }

    override suspend fun confirmAppointment(notificationId: String) {
        delay(500)
        _notifications.update { currentList ->
            currentList.filter { it.id != notificationId }
        }
    }

    override suspend fun rejectAppointment(notificationId: String) {
        delay(500)
        _notifications.update { currentList ->
            currentList.filter { it.id != notificationId }
        }
    }
}
