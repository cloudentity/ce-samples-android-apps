package com.example.authorize

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject


class TokenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)

        val textView = findViewById<TextView>(R.id.tokenResponse)
        if (savedInstanceState == null) {
            val extrasToken = intent.extras?.get("token_response") as String
            if (extrasToken.isEmpty()) {

                textView.text = "Failed to obtain token"
                return
            }
            val spacesToIndentEachLevel = 2
            val s = JSONObject(extrasToken).toString(spacesToIndentEachLevel)
            textView.text = s
        }

//        String newString;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                newString= null;
//            } else {
//                newString= extras.getString("STRING_I_NEED");
//            }
//        } else {
//            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
//        }
    }


}