package com.safepassword.app.data.mappers

import com.safepassword.app.data.models.LoginResponse
import com.safepassword.app.domain.models.AuthResult
import com.safepassword.app.domain.models.User
import javax.inject.Inject

class AuthMapper @Inject constructor() {

    fun toDomain(response: LoginResponse): AuthResult = AuthResult(
        user = User(
            id = response.user.id,
            name = response.user.name,
            email = response.user.email
        ),
        token = response.token
    )
}