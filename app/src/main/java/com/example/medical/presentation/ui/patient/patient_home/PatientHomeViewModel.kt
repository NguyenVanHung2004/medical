package com.example.medical.presentation.ui.patient.patient_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medical.domain.repository.PatientHomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientHomeViewModel(
    private val repository: PatientHomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientHomeUiState())
    val uiState: StateFlow<PatientHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            repository.getUserName().collect { name ->
                _uiState.update { it.copy(userName = name) }
            }
            
            repository.getUpcomingAppointment().collect { appt ->
                _uiState.update { it.copy(upcomingAppointment = appt) }
            }
            
            repository.getPopularSpecialties().collect { specs ->
                _uiState.update { it.copy(specialties = specs) }
            }
            
            repository.getHealthArticles().collect { articles ->
                _uiState.update { it.copy(articles = articles, isLoading = false) }
            }
        }
    }
}
