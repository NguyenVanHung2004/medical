package com.example.medical.data.repository

import com.example.medical.domain.model.PatientProfile
import com.example.medical.domain.model.User
import com.example.medical.domain.model.UserRole
import com.example.medical.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl : ProfileRepository {
    override fun getUserProfile(): Flow<Pair<User, PatientProfile>> = flow {
        delay(1000)
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
