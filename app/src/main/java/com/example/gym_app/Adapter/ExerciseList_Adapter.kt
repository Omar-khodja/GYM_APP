package com.example.gym_app.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.ExerciseLis_Data
import com.example.gym_app.databinding.CustomExerciseBinding


class ExerciseList_Adapter(private val context: Context, val itemlist:MutableList<ExerciseLis_Data>,val OnClikItem :(ExerciseLis_Data)->Unit)
    :RecyclerView.Adapter<ExerciseList_Adapter.ViewHolder>(){
        lateinit var biding:CustomExerciseBinding
        class ViewHolder(val biding:CustomExerciseBinding):RecyclerView.ViewHolder(biding.root){
            fun bind(data:ExerciseLis_Data){
                biding.data = data
               Glide.with(biding.root)
                   .load(data.imagUrl)
                   .into(biding.imageView1)


            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        biding = CustomExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder((biding))
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = itemlist[position]
        holder.bind(data)
        holder.itemView.setOnClickListener{
            OnClikItem(data)
        }
    }
}