package edu.put.inf151892


import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ContextUtils.getActivity


class ConfigActivity : AppCompatActivity() {

    private lateinit var inputUsername: EditText
    private lateinit var usernameButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        val cache = getSharedPreferences("cache", Context.MODE_PRIVATE)

        inputUsername =findViewById(R.id.usernameText)
        usernameButton = findViewById(R.id.usernameButton)
        usernameButton.setOnClickListener{
            val username = inputUsername.text.toString()
            cache.edit().putString("username",username).apply()
            cache.edit().putBoolean("confDone",true).apply()
            val db = DBHandler(this)
        }



    }
}