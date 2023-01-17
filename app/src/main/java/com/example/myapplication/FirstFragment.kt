package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class FirstFragment:Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.aboutappfragment,container, false )

        val title = view?.findViewById<TextView>(R.id.textView3)
        title?.text="About this app"



        val next=view.findViewById<Button>(R.id.moreaboutme)


        next.setOnClickListener{
            val secondFragment=SecondFragment()
            val bundle=Bundle()
            val fragmentTransaction: FragmentTransaction?= activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, secondFragment)
            fragmentTransaction?.commit()

        }
        return view
    }
}