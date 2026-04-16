package com.safepassword.app.data.repositories

import com.safepassword.app.data.api.ApiService
import com.safepassword.app.data.api.safeApiCall
import com.safepassword.app.data.api.ApiResult
import com.safepassword.app.data.mappers.AuthMapper
import com.safepassword.app.data.models.LoginRequest
import com.safepassword.app.domain.models.AuthResult
import com.safepassword.app.domain.models.Credentials
import com.safepassword.app.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: AuthMapper
) : AuthRepository {

    private var savedCredentials: Credentials? = null
    private var loggedIn: Boolean = false

    override suspend fun login(credentials: Credentials): Flow<AuthResult> = flow {
        val result = safeApiCall {
            apiService.login(LoginRequest(credentials.email, credentials.password))
        }
        when (result) {
            is ApiResult.Success -> {
                loggedIn = true
                if (credentials.rememberMe) saveCredentials(credentials)
                emit(mapper.toDomain(result.data))
            }
            is ApiResult.Error -> throw Exception("Erro ${result.code}: ${result.message}")
            is ApiResult.Exception -> throw result.e
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout() {
        loggedIn = false
    }

    override fun isLoggedIn(): Boolean = loggedIn

    override fun getSavedCredentials(): Credentials? = savedCredentials

    override suspend fun saveCredentials(credentials: Credentials) {
        savedCredentials = credentials
    }
}