package com.example.medical.presentation.ui.doctor.notification

import com.example.medical.domain.model.DoctorNotification

data class DoctorNotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<DoctorNotification> = emptyList(),
    val error: String? = null,
    val selectedTab: Int = 0 // 0: Tất cả, 1: Chưa đọc
)
