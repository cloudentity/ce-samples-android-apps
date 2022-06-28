package com.example.oauth2app

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("id_token")
    val idToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("token_type")
    val tokenType: String
)
