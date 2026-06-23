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
                User(
                    id = "u1",
                    email = "nguyenvana@gmail.com",
                    phone = "0987654321",
                    fullName = "Nguyễn Văn A",
                    avatarUrl = "https://i.pravatar.cc/150?u=a042581f4e29026704d",
                    role = UserRole.PATIENT,
                    token = "mock_token"
                ),
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
