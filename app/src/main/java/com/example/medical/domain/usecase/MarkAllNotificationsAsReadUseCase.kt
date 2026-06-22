package com.example.medical.domain.usecase

import com.example.medical.domain.repository.DoctorNotificationRepository

class MarkAllNotificationsAsReadUseCase(
    private val repository: DoctorNotificationRepository
) {
    suspend operator fun invoke() {
        repository.markAllAsRead()
    }
}
