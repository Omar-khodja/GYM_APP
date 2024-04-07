package com.example.gym_app.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.NewMessageData
import com.example.gym_app.databinding.NewmessagesadapterBinding

class NewMessageAdapter( val context: Context,val itemList:List<NewMessageData>,private val onItemClick: (NewMessageData) -> Unit):
    RecyclerView.Adapter<NewMessageAdapter.ViewHolder>() {
    lateinit var biding:NewmessagesadapterBinding


    class ViewHolder(var biding : NewmessagesadapterBinding):RecyclerView.ViewHolder(biding.root) {
        fun bind(users : NewMessageData){
            Glide.with(biding.root)
                .load(Uri.parse(users.imguri))
                .into(biding.imageView1)
            biding.textView.text = users.username

        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        biding = NewmessagesadapterBinding.inflate(
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