package com.safepassword.app.data.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String = "",
    @SerializedName("user") val user: UserResponse = UserResponse()
)

data class UserResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = ""
)