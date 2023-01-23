package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class LocationRecyclerAdapter(val items: ArrayList<MyLocation> ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {

        return LocationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.component,
                parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocationViewHolder -> {
                holder.bind(position, items[position])
            }
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, InfoActivity::class.java)
            intent.putExtra("id",items[position].id )
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }



}

