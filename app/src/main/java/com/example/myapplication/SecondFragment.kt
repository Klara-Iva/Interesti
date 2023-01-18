package com.example.myapplication

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

class SecondFragment:Fragment() {
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.aboutmefragment,container, false )
        var name=view.findViewById<TextView>(R.id.descriptionaboutme)
        var image=view.findViewById<ImageView>(R.id.meimage)

        val docRef = db.collection("about").document("1")
        docRef.get()
            .addOnSuccessListener { document ->
                name.text = document?.data!!["name"].toString()

                Glide.with(this).load(document?.data!!["image"]).into(image)

            }

        val goback=view.findViewById<Button>(R.id.goback)
        goback.setOnClickListener{
            val firstFragment=FirstFragment()
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, firstFragment)
            fragmentTransaction?.commit()


        }
        val backButton2=view.findViewById<ImageButton>(R.id.buttonme)
        backButton2.setOnClickListener(){
            activity?.finish()
        }

        return view

    }
}