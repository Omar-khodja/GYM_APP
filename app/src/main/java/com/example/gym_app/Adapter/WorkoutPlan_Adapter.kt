package com.example.gym_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.MyWorckoutPlna_Data
import com.example.gym_app.WoroutPlanList_Data
import com.example.gym_app.databinding.CustomWorkoutplanBinding

class WorkoutPlan_Adapter(val itemlist:MutableList<WoroutPlanList_Data>, val OnClik : (WoroutPlanList_Data)-> Unit)
    :RecyclerView.Adapter<WorkoutPlan_Adapter.ViewHolder>(){
           lateinit var biding : CustomWorkoutplanBinding
        class ViewHolder(var biding : CustomWorkoutplanBinding):RecyclerView.ViewHolder(biding.root){
            fun bind(data: WoroutPlanList_Data){
                biding.data = data
                Glide.with(biding.root)
                    .load(data.imagUrl)
                    .into(biding.imageView)
            }

        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        biding = CustomWorkoutplanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder((biding))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        var data = itemlist[position]
        holder.bind(data)
        holder.itemView.setOnClickListener{
            OnClik(data)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}