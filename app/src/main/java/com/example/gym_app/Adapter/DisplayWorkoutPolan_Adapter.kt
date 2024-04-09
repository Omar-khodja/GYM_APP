package com.example.gym_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.MyWorckoutPlna_Data
import com.example.gym_app.databinding.DisplayWorkoutplanBinding

class DisplayWorkoutPolan_Adapter(val itemlis:MutableList<MyWorckoutPlna_Data>)
    :RecyclerView.Adapter<DisplayWorkoutPolan_Adapter.ViewHolder>(){
        lateinit var  binding:DisplayWorkoutplanBinding
        class ViewHolder(val binding:DisplayWorkoutplanBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(data : MyWorckoutPlna_Data){
                binding.data = data
                Glide.with(binding.root)
                    .load(data.imagUrl)
                    .into(binding.imageView)


            }
        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DisplayWorkoutPolan_Adapter.ViewHolder {
        binding = DisplayWorkoutplanBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisplayWorkoutPolan_Adapter.ViewHolder, position: Int) {
        var data = itemlis[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int {
        return itemlis.size
    }
}