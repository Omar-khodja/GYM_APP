package com.example.gym_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.NewMessageData
import com.example.gym_app.databinding.CoachCardviewBinding

class Coach_Search_Adapter(val itemlist:MutableList<NewMessageData>)
    :RecyclerView.Adapter<Coach_Search_Adapter.ViewHolder>(){
    lateinit var  biding:CoachCardviewBinding
        class ViewHolder(val biding:CoachCardviewBinding):RecyclerView.ViewHolder(biding.root){
            fun bind(data:NewMessageData){
                biding.data = data
                Glide.with(biding.root)
                    .load(data.imguri)
                    .into(biding.imageView2)

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        biding = CoachCardviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(biding)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemlist[position]
        holder.bind(data)
    }
}