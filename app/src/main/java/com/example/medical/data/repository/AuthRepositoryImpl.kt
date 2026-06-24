package com.example.medical.data.repository

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import com.example.medical.domain.model.UserRole
import com.example.medical.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {
    override fun login(email: String, password: String, isDoctor: Boolean): Flow<Result<User>> = flow {
        emit(Result.Loading)
        // TODO: Replace with actual ApiService call
        delay(1500) // Simulate network delay
        
        if (isDoctor && email == "doctor@gmail.com" && password == "123456") {
            emit(Result.Success(User(id = "DOC001", email = email, phone = null, fullName = "BS. Nguyễn Văn An", avatarUrl = null, role = UserRole.DOCTOR, token = "fake_token_doc")))
        } else if (!isDoctor && email == "test@gmail.com" && password == "123456") {
            emit(Result.Success(User(id = "u1", email = email, phone = null, fullName = "Nguyễn Văn A", avatarUrl = null, role = UserRole.PATIENT, token = "fake_token_xyz")))
        } else {
            emit(Result.Error("Email hoặc mật khẩu không chính xác"))
        }
    }

    override fun register(email: String, phone: String, password: String): Flow<Result<User>> = flow {
        emit(Result.Loading)
        delay(1500)
        
        if (email.isNotBlank() || phone.isNotBlank()) {
            emit(Result.Success(User(id = "2", email = email, phone = phone, fullName = "Người dùng mới", avatarUrl = null, role = com.example.medical.domain.model.UserRole.PATIENT, token = "fake_token_reg")))
        } else {
            emit(Result.Error("Vui lòng nhập Email hoặc Số điện thoại"))
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
