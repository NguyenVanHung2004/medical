package com.example.medical.domain.usecase

import com.example.medical.domain.model.Result
import com.example.medical.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ForgotPasswordUseCase(private val repository: AuthRepository) {
    fun sendOtp(emailOrPhone: String): Flow<Result<Unit>> {
        if (emailOrPhone.isBlank()) {
            return flow { emit(Result.Error("Vui lòng nhập Email hoặc Số điện thoại")) }
        }
        return repository.sendForgotPasswordOtp(emailOrPhone)
    }

    fun verifyOtp(otp: String): Flow<Result<Unit>> {
        if (otp.isBlank()) {
            return flow { emit(Result.Error("Vui lòng nhập mã OTP")) }
        }
        return repository.verifyForgotPasswordOtp(otp)
    }

    fun resetPassword(newPassword: String): Flow<Result<Unit>> {
        if (newPassword.isBlank()) {
            return flow { emit(Result.Error("Vui lòng nhập mật khẩu mới")) }
        }
        if (newPassword.length < 8) {
            return flow { emit(Result.Error("Mật khẩu phải có tối thiểu 8 ký tự")) }
        }
        return repository.resetPassword(newPassword)
    }
}
