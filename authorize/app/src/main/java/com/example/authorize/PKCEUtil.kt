package com.example.authorize

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

object PKCEUtil {

    private var verifier = ""

    fun getCodeVerifier(): String {
        return verifier
    }

    fun getCodeChallenge(): String {
        verifier = generateCodeVerifier()
        return generateCodeChallenge(verifier)
    }

    @Throws(UnsupportedEncodingException::class)
    private fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val codeVerifier = ByteArray(32)
        secureRandom.nextBytes(codeVerifier)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier)
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    private fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(charset("US-ASCII"))
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest: ByteArray = messageDigest.digest()
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
}

