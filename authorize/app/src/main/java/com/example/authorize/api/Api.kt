package com.example.authorize.api

interface AuthApi {
    @FormUrlEncoded
    @POST(TOKEN_ENDPOINT)
    suspend fun getTokeInfo(@Field("client_id") clientID: String, @Field("redirect_uri") redirectURI: String,
                            @Field("grant_type") grantType: String, @Field("code_verifier") verifier: String,
                            @Field("code") code: String): Response<TokenResponse>
}