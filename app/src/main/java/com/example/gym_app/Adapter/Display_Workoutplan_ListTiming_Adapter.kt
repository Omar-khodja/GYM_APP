package com.example.gym_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym_app.WoroutPlan_Data
import com.example.gym_app.databinding.CustomWorkoutlIstBinding

class Display_Workoutplan_ListTiming_Adapter(val itemlist:MutableList<WoroutPlan_Data>, val OnItemClik:(WoroutPlan_Data)->Unit)
        :RecyclerView.Adapter<Display_Workoutplan_ListTiming_Adapter.ViewHolder>(){
            lateinit var biding:CustomWorkoutlIstBinding
    class ViewHolder(val biding:CustomWorkoutlIstBinding):RecyclerView.ViewHolder(biding.root){
        fun bind(data:WoroutPlan_Data){
            biding.data = data
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        biding = CustomWorkoutlIstBinding.inflate(
            LayoutInflater.from(parent.context)
                ,parent
            ,false
        )
        return ViewHolder(biding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemlist[position]
        holder.bind(data)
        holder.itemView.setOnClickListener{
            OnItemClik(data)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

}