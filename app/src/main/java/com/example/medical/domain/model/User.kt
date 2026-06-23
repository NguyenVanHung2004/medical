package com.example.medical.domain.model

enum class UserRole {
    PATIENT, DOCTOR
}

data class User(
    val id: String,
    val email: String,
    val phone: String?,
    val fullName: String,
    val avatarUrl: String?,
    val role: UserRole,
    val token: String? = null
)

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
