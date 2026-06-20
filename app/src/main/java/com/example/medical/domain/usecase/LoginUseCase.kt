package com.example.medical.domain.usecase

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import com.example.medical.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repository: AuthRepository) {
    operator fun invoke(email: String, password: String): Flow<Result<User>> {
        // Validation logic can go here before calling the repository
        if (email.isBlank() || password.isBlank()) {
            return kotlinx.coroutines.flow.flow { 
                emit(Result.Error("Email and password cannot be empty")) 
            }
        }
        return repository.login(email, password)
    }
}
