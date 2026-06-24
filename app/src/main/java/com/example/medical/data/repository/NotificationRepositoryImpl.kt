package com.example.medical.data.repository

import com.example.medical.domain.model.Notification
import com.example.medical.domain.model.NotificationType
import com.example.medical.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NotificationRepositoryImpl : NotificationRepository {
    override fun getNotifications(): Flow<List<Notification>> {
        return MockSharedData.notificationsList
    }
}
