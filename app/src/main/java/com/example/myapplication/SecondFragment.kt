package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class SecondFragment:Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.aboutmefragment,container, false )

        val goback=view.findViewById<Button>(R.id.goback)
        goback.setOnClickListener{
            val firstFragment=FirstFragment()
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, firstFragment)
            fragmentTransaction?.commit()


        }


        return view

    }
}