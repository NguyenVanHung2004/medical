package com.example.medical.presentation.ui.patient.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getNotifications().collect { result ->
                when (result) {
                    is com.example.medical.domain.model.Result.Success -> {
                        _uiState.update { it.copy(notifications = result.data, isLoading = false) }
                    }
                    is com.example.medical.domain.model.Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    is com.example.medical.domain.model.Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllAsRead().collect { result ->
                if (result is com.example.medical.domain.model.Result.Success) {
                    loadNotifications() // reload
                }
            }
        }
    }
}
