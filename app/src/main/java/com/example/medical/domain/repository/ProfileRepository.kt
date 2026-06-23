package com.example.medical.domain.repository

import com.example.medical.domain.model.PatientProfile
import com.example.medical.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserProfile(): Flow<Pair<User, PatientProfile>>
}
