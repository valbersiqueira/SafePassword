package com.safepassword.app.data.models

import com.google.gson.annotations.SerializedName

data class SampleItemResponse(
    @SerializedName("id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("description") val description: String = ""
)

data class SampleItemRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)