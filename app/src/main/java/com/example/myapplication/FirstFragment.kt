package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirstFragment:Fragment() {
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.aboutappfragment,container, false )

        val title = view?.findViewById<TextView>(R.id.textView3)
        title?.text="About this app"

        var name=view.findViewById<TextView>(R.id.descriptionabout)


        val docRef = db.collection("about").document("2")
        docRef.get()
            .addOnSuccessListener { document ->
                name.text = document?.data!!["name"].toString()

            }

        val next=view.findViewById<Button>(R.id.moreaboutme)


        next.setOnClickListener{
            val secondFragment=SecondFragment()

            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, secondFragment)
            fragmentTransaction?.commit()

        }
        val backButton1=view.findViewById<ImageButton>(R.id.buttonapp)
        backButton1.setOnClickListener(){
            activity?.finish()
        }
        return view
    }

}