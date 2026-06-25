package com.example.medical.data.repository

import com.example.medical.domain.model.AppointmentType
import com.example.medical.domain.model.DoctorNotification
import com.example.medical.domain.model.NotificationType
import com.example.medical.domain.repository.DoctorNotificationRepository
import com.example.medical.data.remote.ApiService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow

class DoctorNotificationRepositoryImpl(
    private val apiService: ApiService
) : DoctorNotificationRepository {

    override fun getNotifications(): Flow<List<DoctorNotification>> = flow {
        try {
            val dtos = apiService.getNotifications()
            val notifications = dtos.map { dto ->
                val type = when (dto.type) {
                    "APPOINTMENT_UPDATE" -> NotificationType.UPDATE
                    "REMINDER" -> NotificationType.REMINDER
                    "NEW_APPOINTMENT_REQUEST" -> NotificationType.NEW_APPOINTMENT_REQUEST
                    "APPOINTMENT_CANCELLED" -> NotificationType.APPOINTMENT_CANCELLED
                    "UPCOMING_APPOINTMENT" -> NotificationType.UPCOMING_APPOINTMENT
                    else -> NotificationType.SYSTEM
                }

                val timeAgoStr = try {
                    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
                    sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
                    val date = sdf.parse(dto.time)
                    if (date != null) {
                        val diff = System.currentTimeMillis() - date.time
                        when {
                            diff < 60 * 1000 -> "Vừa xong"
                            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} phút trước"
                            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} giờ trước"
                            diff < 30L * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)} ngày trước"
                            else -> java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(date)
                        }
                    } else "Gần đây"
                } catch (e: Exception) {
                    "Gần đây"
                }

                // Parse message to get patient name if needed, or just use message
                DoctorNotification(
                    id = dto.id,
                    type = type,
                    patientName = dto.title,
                    timeInfo = dto.message,
                    timestamp = timeAgoStr,
                    isRead = dto.isRead,
                    appointmentType = AppointmentType.OFFLINE
                )
            }
            emit(notifications)
        } catch (e: Exception) {
            emit(emptyList()) // Handle error state appropriately if needed
        }
    }

    override suspend fun markAsRead(notificationId: String) {
        // Option to implement individual mark as read API, currently just dummy
    }

    override suspend fun markAllAsRead() {
        try {
            apiService.markAllNotificationsAsRead()
        } catch (e: Exception) {
            // Log error
        }
    }

    override suspend fun confirmAppointment(notificationId: String) {
        // Implement confirm logic by mapping notificationId to appointmentId
    }

    override suspend fun rejectAppointment(notificationId: String) {
        // Implement reject logic
    }
}
