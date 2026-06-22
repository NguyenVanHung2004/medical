package com.example.medical.data.repository

import com.example.medical.domain.model.UserProfile
import com.example.medical.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProfileRepositoryImpl : ProfileRepository {
    override fun getUserProfile(): Flow<UserProfile> {
        return flowOf(
            UserProfile(
                id = "p1",
                fullName = "Trần Thị Bích",
                dob = "25/10/1990",
                gender = "Nữ",
                email = "bich.tran@email.com",
                phone = "090 123 4567",
                address = "123 Đường Lê Lợi, Quận 1, TP. Hồ Chí Minh",
                bloodType = "O+",
                allergies = "Không có",
                avatarUrl = "https://i.pravatar.cc/150?img=5"
            )
        )
    }
}
