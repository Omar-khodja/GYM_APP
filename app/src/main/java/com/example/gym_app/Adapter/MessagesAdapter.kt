package com.example.gym_app.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.MessagesData
import com.example.gym_app.databinding.MessagesDesginBinding

class MessagesAdapter(val context: Context, val itemlist:MutableList<MessagesData>, private val onItemClick: (MessagesData) -> Unit)
    :RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
        lateinit var biding : MessagesDesginBinding
        class ViewHolder(var binding:MessagesDesginBinding):RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(chat: MessagesData){
                Glide.with(binding.root)
                    .load(chat.imagUri)
                    .into(binding.ProfileImag)
                binding.UserName.text = chat.username
                if(chat.sender == "RIGHT") {
                    binding.LatestMessage.text = "You: ${chat.message}"
                }else{
                    binding.LatestMessage.text = chat.message
                    binding.LatestMessage.setTextColor(Color.BLUE)

                }


            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesAdapter.ViewHolder {
        biding = MessagesDesginBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent
            ,false
        )
        return ViewHolder(biding)
    }

    override fun onBindViewHolder(holder: MessagesAdapter.ViewHolder, position: Int) {
        val data = itemlist[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onItemClick(data) // Call the lambda when an item is clicked
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}