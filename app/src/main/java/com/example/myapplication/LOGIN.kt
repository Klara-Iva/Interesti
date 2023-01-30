package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LOGIN : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        var login=findViewById<Button>(R.id.button)
        login.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            true

        }
        supportActionBar?.hide()

    }
}