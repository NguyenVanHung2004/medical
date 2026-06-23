package com.example.medical.domain.usecase

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import com.example.medical.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterUseCase(private val repository: AuthRepository) {
    operator fun invoke(email: String, phone: String, password: String, confirmPassword: String, isPhoneTab: Boolean = false): Flow<Result<User>> {
        if (isPhoneTab) {
            if (phone.isBlank() || password.isBlank()) {
                return flow { emit(Result.Error("Vui lòng nhập Số điện thoại và Mã OTP")) }
            }
            if (!phone.matches(Regex("^[0-9]{10,11}$"))) {
                return flow { emit(Result.Error("Số điện thoại phải từ 10 đến 11 chữ số")) }
            }
            if (password != "123456") {
                return flow { emit(Result.Error("Mã OTP không chính xác")) }
            }
        } else {
            if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                return flow { 
                    emit(Result.Error("Vui lòng nhập đầy đủ thông tin")) 
                }
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return flow { 
                    emit(Result.Error("Định dạng Email không hợp lệ")) 
                }
            }
            if (password.length < 6) {
                return flow {
                    emit(Result.Error("Mật khẩu phải có tối thiểu 6 ký tự"))
                }
            }
            if (password != confirmPassword) {
                return flow {
                    emit(Result.Error("Mật khẩu không khớp"))
                }
            }
        }
        
        return repository.register(email, phone, password)
    }
}
