package com.example.medical.domain.usecase

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import com.example.medical.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterUseCase(private val repository: AuthRepository) {
    operator fun invoke(email: String, phone: String, password: String, isPhoneTab: Boolean = false): Flow<Result<User>> {
        if (isPhoneTab) {
            if (phone.isBlank() || password.isBlank()) {
                return flow { emit(Result.Error("Vui lòng nhập Số điện thoại và Mã OTP")) }
            }
            if (password != "123456") {
                return flow { emit(Result.Error("Mã OTP không chính xác")) }
            }
        } else {
            if (email.isBlank() || password.isBlank()) {
                return flow { 
                    emit(Result.Error("Vui lòng nhập Email và Mật khẩu")) 
                }
            }
            if (password.length < 8) {
                return flow {
                    emit(Result.Error("Mật khẩu phải có tối thiểu 8 ký tự"))
                }
            }
        }
        
        return repository.register(email, phone, password)
    }
}
