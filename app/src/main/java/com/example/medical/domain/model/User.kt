package com.example.medical.domain.model

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val token: String
)

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
