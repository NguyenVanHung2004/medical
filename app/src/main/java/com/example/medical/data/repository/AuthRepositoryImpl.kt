package com.example.medical.data.repository

import com.example.medical.data.local.TokenManager
import com.example.medical.data.remote.ApiService
import com.example.medical.data.remote.LoginRequest
import com.example.medical.data.remote.RegisterRequest
import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import com.example.medical.domain.model.UserRole
import com.example.medical.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {
    override fun login(email: String, password: String, isDoctor: Boolean): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try {
            val role = if (isDoctor) "DOCTOR" else "PATIENT"
            val response = apiService.login(LoginRequest(email, password, role))
            
            tokenManager.saveToken(response.token)
            
            val user = User(
                id = response.user.id,
                email = response.user.email,
                phone = null,
                fullName = response.user.fullName,
                avatarUrl = response.user.avatarUrl,
                role = if (response.user.role == "DOCTOR") UserRole.DOCTOR else UserRole.PATIENT,
                token = response.token
            )
            
            // Cập nhật MockSharedData để giữ tương thích với các Repo chưa migrate
            if (!isDoctor) MockSharedData.mockPatient = user
            
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error("Đăng nhập thất bại: ${e.message ?: "Lỗi mạng"}"))
        }
    }

    override fun register(email: String, phone: String, password: String, isDoctor: Boolean): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try {
            val role = if (isDoctor) "DOCTOR" else "PATIENT"
            val response = apiService.register(
                RegisterRequest(
                    email = email,
                    phone = phone,
                    password = password,
                    fullName = "Người dùng mới", // Default name
                    role = role
                )
            )
            
            tokenManager.saveToken(response.token)
            
            val user = User(
                id = response.user.id,
                email = response.user.email,
                phone = phone,
                fullName = response.user.fullName,
                avatarUrl = response.user.avatarUrl,
                role = if (role == "DOCTOR") UserRole.DOCTOR else UserRole.PATIENT,
                token = response.token
            )
            
            if (!isDoctor) MockSharedData.mockPatient = user
            
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error("Đăng ký thất bại: ${e.message ?: "Lỗi mạng"}"))
        }
    }

    override fun sendForgotPasswordOtp(emailOrPhone: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        delay(1000)
        emit(Result.Success(Unit))
    }

    override fun verifyForgotPasswordOtp(otp: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        delay(1000)
        if (otp == "123456") {
            emit(Result.Success(Unit))
        } else {
            emit(Result.Error("Mã OTP không chính xác"))
        }
    }

    override fun resetPassword(newPassword: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        delay(1000)
        emit(Result.Success(Unit))
    }
}
