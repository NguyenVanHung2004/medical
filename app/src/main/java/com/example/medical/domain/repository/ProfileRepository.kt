package com.example.medical.domain.repository

import com.example.medical.domain.model.PatientProfile
import com.example.medical.domain.model.User
import kotlinx.coroutines.flow.Flow
import com.example.medical.domain.model.Result

interface ProfileRepository {
    fun getUserProfile(): Flow<Pair<User, PatientProfile>>
    fun updateProfile(fullName: String, dob: String, gender: String, address: String, insuranceInfo: String?): Flow<Result<Unit>>
}
