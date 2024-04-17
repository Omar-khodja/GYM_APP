package com.example.gym_app.Adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.Activity.Workout_plan.PlayVideo_Activity
import com.example.gym_app.MyWorckout_Plna_List_Data
import com.example.gym_app.databinding.ExerciseCardviewDesignBinding
import com.example.gym_app.databinding.ExerciseClientCardviewDesignBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class Display_Client_WorkoutPlan_List_Adapter(val itemlis:MutableList<MyWorckout_Plna_List_Data>)
    :RecyclerView.Adapter<Display_Client_WorkoutPlan_List_Adapter.ViewHolder>(){
        lateinit var  binding:ExerciseClientCardviewDesignBinding
        class ViewHolder(private  val binding:ExerciseClientCardviewDesignBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(data : MyWorckout_Plna_List_Data){
                binding.data = data
                Glide.with(binding.root)
                    .load(data.imagUrl)
                    .into(binding.ExerciseVideo)
                binding.cardviewVideo.setOnClickListener{
                    binding.cardView.visibility = View.VISIBLE
                    binding.video.setVideoURI(Uri.parse(data.videoUrl))
                    binding.video.setOnPreparedListener{
                        it.isLooping =true
                        binding.video.start()
                    }
                }
                binding.video.setOnClickListener{
                    if (binding.video.isPlaying) {
                        binding.video.stopPlayback()
                        binding.cardView.visibility = View.INVISIBLE

                    }

                }
            }


        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Display_Client_WorkoutPlan_List_Adapter.ViewHolder {
        binding = ExerciseClientCardviewDesignBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Display_Client_WorkoutPlan_List_Adapter.ViewHolder, position: Int) {
        var data = itemlis[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int {
        return itemlis.size
    }
}