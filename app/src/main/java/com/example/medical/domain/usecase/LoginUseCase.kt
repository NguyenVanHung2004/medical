package com.example.medical.domain.usecase

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import com.example.medical.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repository: AuthRepository) {
    operator fun invoke(email: String, password: String, isDoctor: Boolean = false): Flow<Result<User>> {
        if (email.isBlank() || password.isBlank()) {
            return kotlinx.coroutines.flow.flow { 
                emit(Result.Error("Vui lòng nhập đầy đủ Email và mật khẩu")) 
            }
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return kotlinx.coroutines.flow.flow { 
                emit(Result.Error("Định dạng Email không hợp lệ")) 
            }
        }
        return repository.login(email, password, isDoctor)
    }
}
