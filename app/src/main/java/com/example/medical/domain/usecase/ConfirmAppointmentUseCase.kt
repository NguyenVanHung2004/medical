package com.example.medical.domain.usecase

import com.example.medical.domain.repository.DoctorNotificationRepository

class ConfirmAppointmentUseCase(
    private val repository: DoctorNotificationRepository
) {
    suspend operator fun invoke(notificationId: String) {
        repository.confirmAppointment(notificationId)
    }
}
