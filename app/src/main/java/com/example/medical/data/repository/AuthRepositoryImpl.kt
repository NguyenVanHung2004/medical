package com.example.medical.data.repository

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import com.example.medical.domain.model.UserRole
import com.example.medical.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {
    override fun login(email: String, password: String): Flow<Result<User>> = flow {
        emit(Result.Loading)
        // TODO: Replace with actual ApiService call
        delay(1500) // Simulate network delay
        
        if (email == "test@gmail.com" && password == "123456") {
            emit(Result.Success(User(id = "1", email = email, phone = null, fullName = "Nguyễn Văn A", avatarUrl = null, role = UserRole.PATIENT, token = "fake_token_xyz")))
        } else {
            emit(Result.Error("Email hoặc mật khẩu không chính xác"))
        }
    }

    override fun register(email: String, phone: String, password: String): Flow<Result<User>> = flow {
        emit(Result.Loading)
        // Simulate network delay
        delay(1500)
        
        // Mock validation/success
        if (email.isNotBlank() || phone.isNotBlank()) {
            emit(Result.Success(User(id = "2", email = email, phone = phone, fullName = "Người dùng mới", avatarUrl = null, role = com.example.medical.domain.model.UserRole.PATIENT, token = "fake_token_reg")))
        } else {
            emit(Result.Error("Vui lòng nhập Email hoặc Số điện thoại"))
        }
    }
}
