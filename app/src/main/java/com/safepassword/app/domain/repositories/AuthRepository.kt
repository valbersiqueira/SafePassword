package com.safepassword.app.domain.repositories

import com.safepassword.app.domain.models.AuthResult
import com.safepassword.app.domain.models.Credentials
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(credentials: Credentials): Flow<AuthResult>
    suspend fun logout()
    fun isLoggedIn(): Boolean
    fun getSavedCredentials(): Credentials?
    suspend fun saveCredentials(credentials: Credentials)
}