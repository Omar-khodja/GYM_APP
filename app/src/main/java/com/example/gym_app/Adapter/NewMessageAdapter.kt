package com.example.gym_app.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.NewMessageData
import com.example.gym_app.databinding.CustomUsersBinding

class NewMessageAdapter(val itemList:List<NewMessageData>, private val onItemClick: (NewMessageData) -> Unit):
    RecyclerView.Adapter<NewMessageAdapter.ViewHolder>() {
    lateinit var biding:CustomUsersBinding


    class ViewHolder(var biding : CustomUsersBinding):RecyclerView.ViewHolder(biding.root) {
        fun bind(data : NewMessageData){
            Glide.with(biding.root)
                .load(Uri.parse(data.imguri))
                .into(biding.imageView1)
            biding.data = data

        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        biding = CustomUsersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(biding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onItemClick(data) // Call the lambda when an item is clicked
        }
    }

}