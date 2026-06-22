package com.example.medical.domain.usecase

import com.example.medical.domain.model.DoctorNotification
import com.example.medical.domain.repository.DoctorNotificationRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationsUseCase(
    private val repository: DoctorNotificationRepository
) {
    operator fun invoke(): Flow<List<DoctorNotification>> {
        return repository.getNotifications()
    }
}
