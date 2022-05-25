package com.example.simplepkce.api

import com.example.simplepkce.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {
    // TODO change to parsed endpoint
    @FormUrlEncoded
    @POST("cloudentity-bbray2/mobile/oauth2/token")
    suspend fun getTokeInfo(@Field("client_id") clientID: String, @Field("redirect_uri") redirectURI: String,
                            @Field("grant_type") grantType: String, @Field("code_verifier") verifier: String,
                            @Field("code") code: String): Response<TokenResponse>
}