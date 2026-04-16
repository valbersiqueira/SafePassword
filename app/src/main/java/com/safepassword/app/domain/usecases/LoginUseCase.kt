package com.safepassword.app.domain.usecases

import com.safepassword.app.domain.models.AuthResult
import com.safepassword.app.domain.models.Credentials
import com.safepassword.app.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(credentials: Credentials): Flow<AuthResult> =
        repository.login(credentials)
}