package com.example.gym_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym_app.MessagesData
import com.example.gym_app.databinding.CommentDesingBinding
import com.example.gym_app.databinding.MessagesDesginBinding

class Comment_Adapter(val itemlist:MutableList<MessagesData>, val OnClik:(MessagesData)->Unit)
    :RecyclerView.Adapter<Comment_Adapter.ViewHolder>(){
        lateinit var binding:CommentDesingBinding
        class ViewHolder(val binding:CommentDesingBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(data : MessagesData){

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CommentDesingBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent
            ,false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  itemlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data= itemlist[position]
        holder.bind(data)
        holder.itemView.setOnClickListener{
            OnClik(data)
        }
    }
}