package com.example.simplepkce.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.util.*

object JWTUtils {

    fun getJsonFromSegment(segment: String): String {
        val decoded = base64Decode(segment)
        val json = JsonParser().parse(decodeJWTPart(decoded))
        val gson = GsonBuilder().setPrettyPrinting().create()

        return gson.toJson(json)
    }


    fun base64Decode(base64: String): String {
        return base64.replace("-", "+").replace("_", "/")
    }

    fun decodeJWTPart(part: String) : String {
        val decodedBytes: ByteArray = Base64.getDecoder().decode(part)
        return String(decodedBytes)
    }
}