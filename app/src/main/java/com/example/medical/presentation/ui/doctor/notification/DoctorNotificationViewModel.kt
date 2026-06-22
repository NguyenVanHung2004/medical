package com.example.medical.presentation.ui.doctor.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.ConfirmAppointmentUseCase
import com.example.medical.domain.usecase.GetNotificationsUseCase
import com.example.medical.domain.usecase.MarkAllNotificationsAsReadUseCase
import com.example.medical.domain.usecase.RejectAppointmentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorNotificationViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markAllNotificationsAsReadUseCase: MarkAllNotificationsAsReadUseCase,
    private val confirmAppointmentUseCase: ConfirmAppointmentUseCase,
    private val rejectAppointmentUseCase: RejectAppointmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorNotificationUiState())
    val uiState: StateFlow<DoctorNotificationUiState> = _uiState.asStateFlow()

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        viewModelScope.launch {
            getNotificationsUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { notifications ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = notifications,
                            error = null
                        )
                    }
                }
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            markAllNotificationsAsReadUseCase()
        }
    }

    fun confirmAppointment(notificationId: String) {
        viewModelScope.launch {
            confirmAppointmentUseCase(notificationId)
        }
    }

    fun rejectAppointment(notificationId: String) {
        viewModelScope.launch {
            rejectAppointmentUseCase(notificationId)
        }
    }
}
