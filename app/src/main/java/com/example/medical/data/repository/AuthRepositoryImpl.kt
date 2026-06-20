package com.example.medical.data.repository

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
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
            emit(Result.Success(User(id = "1", email = email, fullName = "Nguyễn Văn A", token = "fake_token_xyz")))
        } else {
            emit(Result.Error("Email hoặc mật khẩu không chính xác"))
        }
    }
}
