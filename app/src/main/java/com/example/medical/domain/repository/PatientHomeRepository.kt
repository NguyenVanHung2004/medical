package com.example.medical.domain.repository

import com.example.medical.domain.model.Appointment
import com.example.medical.domain.model.Article
import com.example.medical.domain.model.Specialty
import kotlinx.coroutines.flow.Flow

interface PatientHomeRepository {
    fun getUpcomingAppointment(): Flow<Appointment?>
    fun getPopularSpecialties(): Flow<List<Specialty>>
    fun getHealthArticles(): Flow<List<Article>>
    fun getUserName(): Flow<String>
}
