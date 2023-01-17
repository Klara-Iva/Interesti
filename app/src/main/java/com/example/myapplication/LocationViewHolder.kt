package com.example.myapplication

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LocationViewHolder (val view: View): RecyclerView.ViewHolder(view)
{

    private val locationImage =
        view.findViewById<ImageView>(R.id.loc_picture)
    private val locationname =
        view.findViewById<TextView>(R.id.location_name)
    private val location =
        view.findViewById<TextView>(R.id.longitude)




    @SuppressLint("SetTextI18n")
    fun bind(
        index: Int,
        location: MyLocation
    )
    {

        Glide.with(view.context).load(location.image).into(locationImage)
        locationname.setText(location.name)
        this.location.setText("location: " +location.lati.toString()+", "+location.long.toString())

}


}