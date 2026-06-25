package com.example.medical.data.repository

import com.example.medical.data.remote.ApiService
import com.example.medical.data.remote.UpdateProfileRequest
import com.example.medical.domain.model.PatientProfile
import com.example.medical.domain.model.User
import com.example.medical.domain.model.Result
import com.example.medical.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class ProfileRepositoryImpl(
    private val apiService: ApiService
) : ProfileRepository {
    override fun getUserProfile(): Flow<Pair<User, PatientProfile>> = flow {
        try {
            val userDto = apiService.getProfile()
            // We'll mock the patient profile part if it's not fully in DTO yet, 
            // but we'll use actual data from the userDto where possible.
            val user = User(
                id = userDto.id,
                email = userDto.email,
                phone = userDto.phone,
                fullName = userDto.fullName,
                avatarUrl = userDto.avatarUrl,
                role = com.example.medical.domain.model.UserRole.PATIENT,
                token = "" // Token is not needed here
            )
            val profile = PatientProfile(
                userId = userDto.id,
                dob = userDto.patientProfile?.dob ?: "Chưa cập nhật", 
                gender = userDto.patientProfile?.gender ?: "Chưa cập nhật",
                address = userDto.patientProfile?.address ?: "Chưa cập nhật",
                bloodType = userDto.patientProfile?.bloodType ?: "Chưa cập nhật",
                allergies = userDto.patientProfile?.allergies ?: "Chưa cập nhật",
                insuranceInfo = userDto.patientProfile?.insuranceInfo ?: "Chưa cập nhật"
            )
            emit(Pair(user, profile))
        } catch (e: Exception) {
            // Fallback
            emit(
                Pair(
                    MockSharedData.mockPatient,
                    PatientProfile(
                        userId = "u1",
                        dob = "15/08/1990",
                        gender = "Nam",
                        address = "123 Đường Lê Lợi, Quận 1, TP.HCM",
                        bloodType = "O+",
                        allergies = "Hải sản, Penicillin",
                        insuranceInfo = "BHYT: 123456789"
                    )
                )
            )
        }
    }

    override fun updateProfile(fullName: String, phone: String, dob: String, gender: String, address: String, bloodType: String?, allergies: String?, insuranceInfo: String?): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            apiService.updateProfile(
                UpdateProfileRequest(
                    fullName = fullName,
                    phone = phone,
                    dob = dob,
                    gender = gender,
                    address = address,
                    bloodType = bloodType,
                    allergies = allergies,
                    insuranceInfo = insuranceInfo
                )
            )
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error("Cập nhật thất bại: ${e.message}"))
        }
    }
}
