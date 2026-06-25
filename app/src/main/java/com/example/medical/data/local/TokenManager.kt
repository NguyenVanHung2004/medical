package com.example.medical.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TokenManager(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val TOKEN_KEY = "jwt_token"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val _tokenFlow = MutableStateFlow(prefs.getString(TOKEN_KEY, null))
    val tokenFlow: Flow<String?> = _tokenFlow

    suspend fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
        _tokenFlow.value = token
    }

    suspend fun deleteToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
        _tokenFlow.value = null
    }
    
    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }
}
