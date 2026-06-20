package com.example.medical.domain.repository

import com.example.medical.domain.model.Result
import com.example.medical.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Result<User>>
}
