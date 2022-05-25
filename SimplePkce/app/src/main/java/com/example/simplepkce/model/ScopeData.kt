package com.example.simplepkce.model

import com.google.gson.annotations.SerializedName

data class ScopeData(
    @SerializedName("scope")
    val scope: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)