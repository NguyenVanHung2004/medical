package com.example.medical.data.repository

import com.example.medical.domain.model.Notification
import com.example.medical.domain.model.NotificationType
import com.example.medical.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NotificationRepositoryImpl : NotificationRepository {
    override fun getNotifications(): Flow<List<Notification>> {
        return flowOf(
            listOf(
                Notification(
                    id = "1",
                    title = "Nhắc nhở lịch hẹn",
                    message = "Bạn có lịch hẹn với BS. Nguyễn Văn An lúc 09:00 sáng mai (25/10/2024). Vui lòng đến trước 15 phút.",
                    timeAgo = "2 giờ trước",
                    isUnread = true,
                    type = NotificationType.REMINDER
                ),
                Notification(
                    id = "2",
                    title = "Cập nhật lịch hẹn",
                    message = "Lịch hẹn của bạn với BS. Trần Thị Bích đã được dời sang 14:00 ngày 26/10/2024 do yêu cầu từ bác sĩ.",
                    timeAgo = "1 ngày trước",
                    isUnread = false,
                    type = NotificationType.UPDATE
                ),
                Notification(
                    id = "3",
                    title = "Nhắc nhở tái khám",
                    message = "Đã đến lúc đặt lịch tái khám với BS. Lê Văn C để kiểm tra lại tình trạng dị ứng da của bạn.",
                    timeAgo = "3 ngày trước",
                    isUnread = false,
                    type = NotificationType.REMINDER
                ),
                Notification(
                    id = "4",
                    title = "Có khung giờ trống sớm hơn",
                    message = "BS. Phạm Thị D có khung giờ trống lúc 10:00 sáng nay. Bạn có muốn đổi lịch khám lên sớm không?",
                    timeAgo = "1 tuần trước",
                    isUnread = false,
                    type = NotificationType.SYSTEM
                )
            )
        )
    }
}
