package com.example.medical.presentation.ui.patient.booking_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.model.DoctorDetail
import com.example.medical.domain.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookingSuccessUiState(
    val doctor: DoctorDetail? = null,
    val date: String = "",
    val time: String = "",
    val referenceCode: String = "#MC-8942-TX",
    val location: String = "Phòng khám số 12, Tầng 3, Tòa nhà A"
)

class BookingSuccessViewModel(
    private val repository: DoctorRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val doctorId: String = checkNotNull(savedStateHandle["doctorId"])
    private val date: String = android.net.Uri.decode(checkNotNull(savedStateHandle["date"]))
    private val time: String = android.net.Uri.decode(checkNotNull(savedStateHandle["time"]))
    private val type: String = checkNotNull(savedStateHandle["type"])

    private val _uiState = MutableStateFlow(BookingSuccessUiState(date = date, time = time))
    val uiState: StateFlow<BookingSuccessUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getDoctorById(doctorId).collect { doctor ->
                _uiState.update { it.copy(doctor = doctor) }
                if (doctor != null) {
                    bookAppointmentUseCase(
                        doctorId = doctor.id,
                        date = date,
                        timeRange = time,
                        reason = "Khám bệnh", // Can be updated if UI adds a reason field
                        type = type.uppercase() // ONLINE or OFFLINE
                    )
                }
            }
        }
    }
}
