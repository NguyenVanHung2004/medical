package com.example.medical.data.repository

import com.example.medical.domain.model.Notification
import com.example.medical.domain.model.NotificationType
import com.example.medical.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

import com.example.medical.data.remote.ApiService
import kotlinx.coroutines.flow.flow
import com.example.medical.domain.model.Result

class NotificationRepositoryImpl(
    private val apiService: ApiService
) : NotificationRepository {
    override fun getNotifications(): Flow<Result<List<Notification>>> = flow {
        emit(Result.Loading)
        try {
            val dtos = apiService.getNotifications()
            val notifications = dtos.map { dto ->
                val type = when(dto.type) {
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

                Notification(
                    id = dto.id,
                    title = dto.title,
                    message = dto.message,
                    timeAgo = timeAgoStr,
                    isUnread = !dto.isRead,
                    type = type
                )
            }
            emit(Result.Success(notifications))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to get notifications"))
        }
    }

    override fun markAllAsRead(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            apiService.markAllNotificationsAsRead()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to mark as read"))
        }
    }
}
