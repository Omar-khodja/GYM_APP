package com.example.gym_app.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.Activity.Workout_plan.PlayVideo_Activity
import com.example.gym_app.MyWorckoutPlna_Data
import com.example.gym_app.databinding.DisplayWorkoutplanBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class DisplayWorkoutPolan_Adapter(val itemlis:MutableList<MyWorckoutPlna_Data>)
    :RecyclerView.Adapter<DisplayWorkoutPolan_Adapter.ViewHolder>(){
        lateinit var  binding:DisplayWorkoutplanBinding
        class ViewHolder(val binding:DisplayWorkoutplanBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(data : MyWorckoutPlna_Data){
                binding.data = data
                Glide.with(binding.root)
                    .load(data.imagUrl)
                    .into(binding.imageView)
                binding.btn.setOnClickListener{
                    var intent = Intent(binding.root.context, PlayVideo_Activity::class.java)
                    intent.putExtra("videUrl",data.videoUrl)
                    binding.root.context.startActivity(intent)

                }
                binding.writeNote.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        var auth = Firebase.auth
                        var db = FirebaseFirestore.getInstance()
                        var note = binding.writeNote.text.toString()
                        if(note.isNotEmpty()) {
                            db.collection("Coach_Workout_Plan/")
                                .document(auth.currentUser?.uid.toString())
                                .collection("Workoutplan")
                                .document(data.listname)
                                .collection(data.listname)
                                .document(data.id).update("Note", note)
                            binding.Note.text = note
                            binding.writeNote.text.clear()
                            binding.writeNote.visibility = View.INVISIBLE
                        }
                    }
                    true
                }
                binding.Notetitle.setOnClickListener{
                    binding.writeNote.visibility = View.VISIBLE
                }
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