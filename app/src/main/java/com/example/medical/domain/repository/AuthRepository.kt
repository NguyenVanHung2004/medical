package com.example.medical.domain.repository

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String, isDoctor: Boolean = false): Flow<Result<User>>
    fun register(email: String, phone: String, password: String, isDoctor: Boolean = false): Flow<Result<User>>
    
    // Forgot Password Flow
    fun sendForgotPasswordOtp(emailOrPhone: String): Flow<Result<Unit>>
    fun verifyForgotPasswordOtp(otp: String): Flow<Result<Unit>>
    fun resetPassword(newPassword: String): Flow<Result<Unit>>
}
