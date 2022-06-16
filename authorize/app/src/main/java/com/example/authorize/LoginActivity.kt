package com.example.authorize

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.authorize.Config.CLIENT_ID
import com.example.authorize.Config.GRANT_TYPE
import com.example.authorize.Config.REDIRECT_URI
import com.example.authorize.Config.TOKEN_ENDPOINT
import com.example.authorize.api.RetrofitInstance
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


class LoginActivity : AppCompatActivity() {

    private val tag = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var signInButton = findViewById<Button>(R.id.signInButton)
        signInButton.setOnClickListener {
            var authManager = AuthManager()
            authManager.initiateAuth(this)

        }
    }

    private fun handleCallback(uri: Uri) {
        val code = uri.getQueryParameter("code")
        if (code?.isEmpty() == false) {
            Log.d(tag, "Code is $code")
            getToken(code)
        } else {
            Log.d(tag, "No Code")
        }
    }

    suspend fun getToken(code: String) {
        val res = RetrofitInstance.api.getTokeInfo(CLIENT_ID, REDIRECT_URI, GRANT_TYPE, PKCEHelper.getCodeVerifier(), code)
        val client = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .add("client_id", CLIENT_ID)
            .add("redirect_uri", REDIRECT_URI)
            .add("grant_type", GRANT_TYPE)
            .add("code_verifier", PKCEHelper.getCodeVerifier())
            .add("code", code)
            .build()

        val request = Request.Builder()
            .url(TOKEN_ENDPOINT)
            .post(formBody)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        Thread {
            val response = client.newCall(request).execute()

            Log.d(tag, response.code.toString())
            if (response.body != null) {
                response.body?.string()?.let {
                    Log.d(tag, it)
                    val intent = Intent(this, TokenActivity::class.java)
                    intent.putExtra("token_response", it)
                    startActivity(intent)
                }

            }

        }.start()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            Log.d(tag, "called me back with $intent")
            val callbackURI = Uri.parse(intent.toString())
            handleCallback(callbackURI)
        }
    }
}

