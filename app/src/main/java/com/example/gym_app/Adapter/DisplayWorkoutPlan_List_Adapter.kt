package com.example.gym_app.Adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.Activity.Workout_plan.PlayVideo_Activity
import com.example.gym_app.MyWorckout_Plna_List_Data
import com.example.gym_app.R
import com.example.gym_app.databinding.ExerciseCardviewDesignBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class DisplayWorkoutPlan_List_Adapter(val itemlis:MutableList<MyWorckout_Plna_List_Data>)
    :RecyclerView.Adapter<DisplayWorkoutPlan_List_Adapter.ViewHolder>(){
        lateinit var  binding:ExerciseCardviewDesignBinding
        class ViewHolder(private  val binding:ExerciseCardviewDesignBinding):RecyclerView.ViewHolder(binding.root){
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
                binding.cardView.setOnClickListener{
                    binding.video.stopPlayback()
                    binding.cardView.visibility = View.INVISIBLE

                }
                //set coach note to database
                binding.savebtn.setOnClickListener {
                        var auth = Firebase.auth
                        var db = FirebaseFirestore.getInstance()
                        var sets = binding.Sets.text.toString()
                        var repsPerSets = binding.repsPerSets.text.toString()
                        if(sets.isNotEmpty()&&repsPerSets.isNotEmpty()) {
                           var collection =  db.collection("Coach_Workout_Plan/")
                                .document(auth.currentUser?.uid.toString())
                                .collection("Workoutplan")
                                .document(data.workoutplan)
                                .collection("List")
                                .document(data.listname)
                                .collection(data.listname)
                                .document(data.id)

                            val updates = hashMapOf(
                                "sets" to sets,
                                "repsets" to repsPerSets
                            )

                            // Update both fields in a single call
                            collection.update(updates as Map<String, Any>)
                                binding.savebtn.text = "Saved"
                                binding.savebtn.background = ContextCompat.getDrawable(binding.root.context, R.drawable.unablebtn)
                                binding.savebtn.isEnabled = false


                        }
                }
            }


        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DisplayWorkoutPlan_List_Adapter.ViewHolder {
        binding = ExerciseCardviewDesignBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisplayWorkoutPlan_List_Adapter.ViewHolder, position: Int) {
        var data = itemlis[position]
        holder.bind(data)

    }

    override fun getItemCount(): Int {
        return itemlis.size
    }
}