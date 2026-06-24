package com.example.medical.domain.repository

import com.example.medical.domain.model.PatientProfile
import com.example.medical.domain.model.User
import kotlinx.coroutines.flow.Flow
import com.example.medical.domain.model.Result

interface ProfileRepository {
    fun getUserProfile(): Flow<Pair<User, PatientProfile>>
    fun updateProfile(fullName: String, phone: String, dob: String, gender: String, address: String, bloodType: String?, allergies: String?, insuranceInfo: String?): Flow<Result<Unit>>
}
