package com.example.sample_android_oauth_authorization_app

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.util.*

object JWTUtil {
    // Gets the JSON from a token segment.
    fun getJsonFromSegment(segment: String): String {
        val json = JsonParser().parse(decodeJWTPart(segment))
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(json)
    }

    // Decodes the JWT segment to JSON object then return as string
    private fun decodeJWTPart(segment: String) : String {
        val cleanedBase64 = segment.replace("-", "+").replace("_", "/")
        val decodedBytes: ByteArray = Base64.getDecoder().decode(cleanedBase64)
        return String(decodedBytes)
    }
}