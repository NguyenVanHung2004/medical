package com.example.medical.domain.model

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isUnread: Boolean,
    val type: NotificationType
)

enum class NotificationType {
    REMINDER, UPDATE, SYSTEM, NEW_APPOINTMENT_REQUEST, APPOINTMENT_CANCELLED, UPCOMING_APPOINTMENT
}
