package com.example.myapplication

import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class LocationViewHolder (val view: View): RecyclerView.ViewHolder(view)
{

    private val locationImage =
        view.findViewById<ImageView>(R.id.loc_picture)
    private val locationname =
        view.findViewById<TextView>(R.id.location_name)
    private val pz =
        view.findViewById<TextView>(R.id.prikazavgzanimljivost)
    private val pp =
        view.findViewById<TextView>(R.id.prikazavgpristupacnost)




    @SuppressLint("SetTextI18n")
    fun bind(
        index: Int,
        location: MyLocation
    )
    {

        Glide.with(view.context).load(location.image).into(locationImage)
        locationname.setText(location.name)
        pz.setText("zanimljivost: "+ DecimalFormat("#.00").format(location.avgZanimljivost)+"⭐")
        pp.setText("pristupačnost "+ DecimalFormat("#.00").format(location.avgPristupacnost)+"⭐")


            }

       // this.prosjek.setText("location: " +location.lati.toString()+", "+location.long.toString())

}

