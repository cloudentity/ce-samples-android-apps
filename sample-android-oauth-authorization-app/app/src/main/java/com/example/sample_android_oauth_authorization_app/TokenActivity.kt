package com.example.sample_android_oauth_authorization_app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject


class TokenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)

        // Get reference to the text view for displaying result.
        val idTokenTextView = findViewById<TextView>(R.id.idToken)
        if (savedInstanceState == null) {
            // The payload is passed via intent. Verify it isn't empty and display it.
            val extra = intent.extras?.get(getString(R.string.payload)) as String
            if (extra.isEmpty()) {
                idTokenTextView.text = getString(R.string.failed_token_obtain)
                return
            }

            idTokenTextView.text = JSONObject(extra).toString(2).toString()
        } else {
            idTokenTextView.text = savedInstanceState.getSerializable(getString(R.string.payload)).toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}