package com.example.simplepkce.util

//object Config {
//    const val BASE_URL = "<base URL for your tenant>"
//    const val AUTHORIZE_ENDPOINT = "<authorize endpoint for your Cloudentity client application>"
//    const val TOKEN_ENDPOINT = "<token endpoint for your Cloudentity client application>"
//    const val CLIENT_ID = "<client ID for your Cloudentity client application>"
//    const val REDIRECT_URI = "oauth://simple-pkce.example.com" // if edited, update your Cloudentity client application redirect_uri
//    const val GRANT_TYPE = "authorization_code"
//}

object Config {
    const val BASE_URL = "https://sample.us.authz.stage.cloudentity.io/"
    const val AUTHORIZE_ENDPOINT = "https://sample.us.authz.stage.cloudentity.io/sample/demo/oauth2/authorize"
    const val TOKEN_ENDPOINT = "https://sample.us.authz.stage.cloudentity.io/sample/demo/oauth2/token"
    const val CLIENT_ID = "caf0k1abso91dunasm9g"
    const val REDIRECT_URI = "oauth://simple-pkce.example.com" // if edited, update your Cloudentity client application redirect_uri
    const val GRANT_TYPE = "authorization_code"
}