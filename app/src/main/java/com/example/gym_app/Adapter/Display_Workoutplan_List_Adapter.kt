package com.example.gym_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gym_app.WoroutPlanList_Data
import com.example.gym_app.databinding.CustomWorkoutlIstBinding

class Display_Workoutplan_List_Adapter(val itemlist:MutableList<WoroutPlanList_Data>,val OnItemClik:(WoroutPlanList_Data)->Unit)
        :RecyclerView.Adapter<Display_Workoutplan_List_Adapter.ViewHolder>(){
            lateinit var biding:CustomWorkoutlIstBinding
    class ViewHolder(val biding:CustomWorkoutlIstBinding):RecyclerView.ViewHolder(biding.root){
        fun bind(data:WoroutPlanList_Data){
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