package com.example.medical.presentation.ui.doctor.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.usecase.doctor.GetDoctorAppointmentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoctorAppointmentViewModel(
    private val getDoctorAppointmentsUseCase: GetDoctorAppointmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorAppointmentUiState())
    val uiState: StateFlow<DoctorAppointmentUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getDoctorAppointmentsUseCase().collect { data ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pendingRequests = data.pendingRequests,
                        scheduledAppointments = data.scheduledAppointments
                    )
                }
            }
        }
    }

    fun handleRequestAction(requestId: String, isAccept: Boolean) {
        // Giả lập logic architecture: Gọi repository/usecase để update status.
        // Ở đây ta update UI State để phản hồi ngay lập tức (Mock).
        val currentPending = _uiState.value.pendingRequests
        val requestToMove = currentPending.find { it.id == requestId } ?: return
        val updatedPending = currentPending.filter { it.id != requestId }
        
        if (isAccept) {
            val newAppointment = com.example.medical.domain.model.Appointment(
                id = requestToMove.id,
                patientName = requestToMove.patientName,
                patientInitial = requestToMove.patientInitial,
                patientGender = "N/A", // Mock data
                patientAge = 0, // Mock data
                patientIdStr = "N/A", // Mock data
                patientAvatarUrl = null,
                date = requestToMove.timeRange.substringBefore(","), // Extract date from timeRange if formatted like "Ngày mai, 09:00 - 09:30"
                timeRange = requestToMove.timeRange.substringAfter(", ").trim(),
                reason = requestToMove.reason,
                location = requestToMove.location,
                status = com.example.medical.domain.model.AppointmentStatus.UPCOMING,
                type = requestToMove.type,
                doctor = com.example.medical.domain.model.Doctor(
                    id = "DOC001",
                    name = "BS. Nguyễn Văn An",
                    avatarUrl = null
                )
            )
            
            val updatedScheduled = _uiState.value.scheduledAppointments.toMutableList()
            updatedScheduled.add(0, newAppointment) // Thêm lên đầu danh sách
            
            _uiState.update { it.copy(pendingRequests = updatedPending, scheduledAppointments = updatedScheduled) }
        } else {
            _uiState.update { it.copy(pendingRequests = updatedPending) }
        }
    }
}
