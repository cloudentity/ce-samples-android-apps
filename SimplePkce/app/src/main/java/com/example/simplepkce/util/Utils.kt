package com.example.simplepkce.util

import android.content.Context
import com.example.simplepkce.model.ScopeData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.NumberFormatException

object Utils {
    private fun getJsonStringFromAssets(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getScopeData(context: Context): List<ScopeData>? {
        val jsonString = getJsonStringFromAssets(context, "scopeData.json")
        val gson = GsonBuilder().create()
        return gson.fromJson(jsonString, Array<ScopeData>::class.java).toList()
    }

    fun isNum(s: String): Boolean {
        return try {
            s.toInt()
            true
        } catch (ex: NumberFormatException) {
            false
        }
    }
}