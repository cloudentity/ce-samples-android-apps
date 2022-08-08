package com.example.sample_android_oauth_authorization_app

object Config {
    const val AUTHORIZE_ENDPOINT = "<sample-android-oauth-authorization-app endpoint for your Cloudentity client application>"
    const val TOKEN_ENDPOINT = "<token endpoint for your Cloudentity client application>"
    const val CLIENT_ID = "<client ID for your Cloudentity client application>"
    const val REDIRECT_URI = "oauth://simple-pkce.example.com" // if edited, update your Cloudentity client application redirect_uri
    const val GRANT_TYPE = "authorization_code"
}