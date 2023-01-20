package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class InfoFragmentContainer: AppCompatActivity()  {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.initialfragmentlayout)

        val firstFragment = FirstFragment()
        val manager = supportFragmentManager
        manager.beginTransaction().apply {

            replace(R.id.fragmentContainerView, firstFragment)
            commit()
        }


    }
}