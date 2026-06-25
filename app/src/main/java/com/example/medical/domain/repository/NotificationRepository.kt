package com.example.medical.domain.repository

import com.example.medical.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<com.example.medical.domain.model.Result<List<Notification>>>
    fun markAllAsRead(): Flow<com.example.medical.domain.model.Result<Unit>>
}
