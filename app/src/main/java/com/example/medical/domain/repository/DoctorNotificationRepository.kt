package com.example.medical.domain.repository

import com.example.medical.domain.model.DoctorNotification
import kotlinx.coroutines.flow.Flow

interface DoctorNotificationRepository {
    fun getNotifications(): Flow<List<DoctorNotification>>
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
    suspend fun confirmAppointment(notificationId: String)
    suspend fun rejectAppointment(notificationId: String)
}
