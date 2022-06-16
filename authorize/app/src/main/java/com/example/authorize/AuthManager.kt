package com.example.authorize

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.example.authorize.Config.AUTHORIZE_ENDPOINT
import com.example.authorize.Config.CLIENT_ID
import com.example.authorize.Config.REDIRECT_URI
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

class AuthManager() {

    fun initiateAuth(ctx: Context) {
        fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

        var authParams = mapOf("client_id" to CLIENT_ID,
            "redirect_uri" to REDIRECT_URI,
            "response_type" to "code",
            "scope" to "email openid profile",
            "code_challenge" to PKCEHelper.getCodeChallenge(),
            "code_challenge_method" to "S256"
        ).map {(k,v) -> "${(k.utf8())}=${v.utf8()}"}.joinToString("&")

        CustomTabsIntent.Builder().build().launchUrl(ctx, Uri.parse("$AUTHORIZE_ENDPOINT?$authParams"))
    }
}



object PKCEHelper {

    private var verifier = ""

    fun getCodeVerifier(): String {
        if (verifier.isEmpty()) {
            verifier = generateCodeVerifier()
        }
        return verifier
    }

    fun getCodeChallenge(): String {
        if (verifier.isEmpty()) {
            verifier = generateCodeVerifier()
        }
        return generateCodeChallange(verifier)
    }

    @Throws(UnsupportedEncodingException::class)
    private fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val codeVerifier = ByteArray(32)
        secureRandom.nextBytes(codeVerifier)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier)
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    private fun generateCodeChallange(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(charset("US-ASCII"))
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest: ByteArray = messageDigest.digest()
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
}