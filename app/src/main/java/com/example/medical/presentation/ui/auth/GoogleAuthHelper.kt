package com.example.medical.presentation.ui.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GoogleAuthHelper {

    // TODO: Thay thế bằng Web Client ID thực sự của bạn từ Google Cloud Console
    const val WEB_CLIENT_ID = "220192180813-r6ga2cgr7o0b35099q70dkhrcfuq35ih.apps.googleusercontent.com"

    /**
     * Thực hiện luồng đăng nhập Google sử dụng Credential Manager.
     * Trả về Google ID Token nếu thành công, hoặc null nếu thất bại/người dùng huỷ.
     */
    suspend fun doGoogleLogin(context: Context): String? {
        val credentialManager = CredentialManager.create(context)
        
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()
            
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
            
        return try {
            val result = withContext(Dispatchers.Main) { // getCredential should be called on Main thread, it handles its own UI coroutines
                credentialManager.getCredential(
                    request = request,
                    context = context,
                )
            }
            
            val credential = result.credential
            
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                Log.d("GoogleAuth", "Đăng nhập Google thành công. Token: ${googleIdTokenCredential.idToken}")
                googleIdTokenCredential.idToken
            } else {
                Log.e("GoogleAuth", "Loại credential không mong muốn")
                null
            }
        } catch (e: GetCredentialException) {
            Log.e("GoogleAuth", "Lỗi lấy Credential: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Lỗi không xác định: ${e.message}")
            null
        }
    }
}
