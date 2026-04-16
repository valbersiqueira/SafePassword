package com.safepassword.app.domain.models

data class User(
    val id: String,
    val name: String,
    val email: String
)

data class Credentials(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)

data class AuthResult(
    val user: User,
    val token: String
)