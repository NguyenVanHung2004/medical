package com.example.medical.presentation.ui.patient.notifications

import com.example.medical.domain.model.Notification

data class NotificationsUiState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false
)
