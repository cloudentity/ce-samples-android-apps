package com.example.simplepkce.repository

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.example.simplepkce.R
import com.example.simplepkce.api.RetrofitInstance
import com.example.simplepkce.model.ScopeData
import com.example.simplepkce.model.TokenResponse
import com.example.simplepkce.util.PKCEHelper
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.Options
import com.yakivmospan.scytale.Store
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import java.net.URLEncoder

open class Repository(context: Context) {

    private var ctx: Context? = null

    init {
        this.ctx = context
    }

    companion object : SingletonHolder<Repository, Context>(::Repository)

    suspend fun getTokenInfo(clientID: String, redirectURI: String, grantType: String, verifier: String, code: String): Response<TokenResponse> {
        return RetrofitInstance.api.getTokeInfo(clientID, redirectURI, grantType, verifier, code)
    }

    fun initiateAuth(ctx: Context, clientID: String, redirectURI: String, authURL: String) {
        fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

        var authParams = mapOf("client_id" to clientID,
            "redirect_uri" to redirectURI,
            "response_type" to "code",
            "scope" to "email openid profile",
            "code_challenge" to PKCEHelper.getCodeChallenge(),
            "code_challenge_method" to "S256"
        ).map {(k,v) -> "${(k.utf8())}=${v.utf8()}"}.joinToString("&")

        CustomTabsIntent.Builder().build().launchUrl(ctx, Uri.parse("$authURL?$authParams"))
    }

    fun getResource(scope: ScopeData, tokenInfo: TokenResponse, cb: (result: String?)-> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(scope.url)
            .get()
            .addHeader("Authorization", "Bearer ${tokenInfo.accessToken}")
            .build()

        Thread {
            val response = client.newCall(request).execute()
            if (response.code != 200) {
                cb.invoke(response.code.toString())
            } else {
                cb.invoke(response.body?.string())
            }
        }.start()
    }

    var iv: ByteArray? = null

    fun storeTokenInfo(tokenInfoJsonString: String) {
        val context = this.ctx ?: return

        val store = Store(context)
        if (!store.hasKey(context.getString(R.string.token_store_alias))) {
            store.generateSymmetricKey(context.getString(R.string.token_store_alias), null)
        }

        val key = store.getSymmetricKey(context.getString(R.string.token_store_alias), null)
        val crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)

        val encryptedToken = crypto.encrypt(tokenInfoJsonString, key)
        val prefs = context.getSharedPreferences("TokenStore", Context.MODE_PRIVATE)
        with (prefs.edit()) {
            putString("TokeInfo", encryptedToken)
            apply()
        }
    }

    fun getTokenInfoStored(): String {
        val context = this.ctx ?: return ""

        val store = Store(context)
        val key = store.getSymmetricKey(context.getString(R.string.token_store_alias), null)
        val crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)
        val prefs = context.getSharedPreferences("TokenStore", Context.MODE_PRIVATE)
        val tokenInfo = prefs.getString("TokeInfo", null) ?: return ""

        return crypto.decrypt(tokenInfo, key)
    }


}