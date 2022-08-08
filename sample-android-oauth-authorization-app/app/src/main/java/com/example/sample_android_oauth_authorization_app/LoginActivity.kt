package com.example.sample_android_oauth_authorization_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.sample_android_oauth_authorization_app.Config.CLIENT_ID
import com.example.sample_android_oauth_authorization_app.Config.GRANT_TYPE
import com.example.sample_android_oauth_authorization_app.Config.REDIRECT_URI
import com.example.sample_android_oauth_authorization_app.Config.TOKEN_ENDPOINT
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.net.URLEncoder


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.signInButton).setOnClickListener {
            fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

            val authParams = mapOf("client_id" to CLIENT_ID,
                "redirect_uri" to REDIRECT_URI,
                "response_type" to "code",
                "scope" to "email openid profile",
                "code_challenge" to PKCEUtil.getCodeChallenge(), // Set the code challenge
                "code_challenge_method" to "S256"
            ).map {(k,v) -> "${(k.utf8())}=${v.utf8()}"}.joinToString("&")

            // Initiate the OAuth 2.0 flow using CustomTabs<url>.
            // Check that CustomTabs are supported!
            CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse("${Config.AUTHORIZE_ENDPOINT}?$authParams"))
        }
    }

    // Check for a valid code in the query params or an error.
    private fun handleCallback(uri: Uri) {
        val code = uri.getQueryParameter("code")
        if (code?.isEmpty() == false) {
            getToken(code)
        }
    }

    // Call the token endpoint.
    private fun getToken(code: String) {
        val client = OkHttpClient()
        /* Previously we sent the code challenge. Now we send the code verifier used
           to generate the code challenge.
        */

        // Get the form body passing in the code verifier.
        val formBody: RequestBody = FormBody.Builder()
            .add("client_id", CLIENT_ID)
            .add("redirect_uri", REDIRECT_URI)
            .add("grant_type", GRANT_TYPE)
            .add("code_verifier", PKCEUtil.getCodeVerifier()) // Send the code verifier
            .add("code", code)
            .build()

        // Build the request ensuring the content type is set to `application/x-www-form-urlencoded`
        // and the form body
        val request = Request.Builder()
            .url(TOKEN_ENDPOINT)
            .post(formBody)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        // Perform the token request on a background thread.
        Thread {
            val response = client.newCall(request).execute()
            if (response.body != null) {
                val gson = Gson()
                // Marshal the response from the token endpoint.
                val tokenResponse = gson.fromJson(response.body?.string(), TokenResponse::class.java)
                // Split the ID Token to get attempt to get the payload.
                val segments = tokenResponse.idToken?.split(".")?.toTypedArray()

                // Create an intent for starting the `TokenActivity`.
                val intent = Intent(this, TokenActivity::class.java)
                // Verify the ID Token has the correct number of segments.
                if (segments.size == 3) {
                    // Extract the payload from ID Token segments and include in the intent.
                    val idToken = JWTUtil.getJsonFromSegment(segments[1])
                    intent.putExtra(getString(R.string.payload), idToken)
                } else {
                    // Invalid number of segments so ignore the token as it is invalid.
                    intent.putExtra(getString(R.string.payload), "Invalid Payload")
                }

                startActivity(intent)
            }

        }.start()
    }

    /* When the user finished signing in and accepting/rejecting consent
       the application will be called, via the redirect URI we created,
       and the intent will include the redirect URI with an error or authorization code.
  */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            val callbackURI = Uri.parse(intent.toString())
            handleCallback(callbackURI)
        }
    }
}

