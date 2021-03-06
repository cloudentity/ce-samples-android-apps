package com.example.simplepkce.util

object Config {
    const val BASE_URL = "<base URL for your tenant>"
    const val AUTHORIZE_ENDPOINT = "<authorize endpoint for your Cloudentity client application>"
    const val TOKEN_ENDPOINT = "<token endpoint for your Cloudentity client application>"
    const val CLIENT_ID = "<client ID for your Cloudentity client application>"
    const val REDIRECT_URI = "oauth://simple-pkce.example.com" // if edited, update your Cloudentity client application redirect_uri
    const val GRANT_TYPE = "authorization_code"
}