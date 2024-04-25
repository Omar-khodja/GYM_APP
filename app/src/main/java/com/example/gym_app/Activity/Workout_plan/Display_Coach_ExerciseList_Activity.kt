package com.example.gym_app.Activity.Workout_plan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.DisplayWorkoutPlan_List_Adapter
import com.example.gym_app.Adapter.ExerciseList_Adapter
import com.example.gym_app.Singlton.DisplayVideo_Singlton
import com.example.gym_app.ExerciseLis_Data
import com.example.gym_app.MyWorckout_Plna_List_Data
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityDisplayexercuselistVideosBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Display_Coach_ExerciseList_Activity : AppCompatActivity() {
    lateinit var binding: ActivityDisplayexercuselistVideosBinding
    lateinit var db: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var Title: String
    lateinit var planeName: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_displayexercuselist_videos)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_displayexercuselist_videos)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        planeName = intent.getStringExtra("planName").toString()
        Title = intent.getStringExtra("Title").toString()
        supportActionBar?.title = "DisplayExerciseList_Activity"
        var collection = db.collection("/Coach_Workout_Plan/${User.instance?.UserId}/Workoutplan/$planeName/List/$Title/$Title")
        val itemList = mutableListOf<MyWorckout_Plna_List_Data>()

        collection.get().addOnSuccessListener { documentSnapshot ->
            for (doc in documentSnapshot.documents) {
                val id = doc.id
                val imagUrl = doc.getString("ImahUrl").toString()
                val Title = doc.getString("VideoName").toString()
                val videoUrl = doc.getString("VideoUrl").toString()
                val CoachId = doc.getString("CoachId").toString()
                val note = doc.getString("Note").toString()
                val sets = doc.getString("sets").toString()
                val repsets = doc.getString("repsets").toString()
                val listname = doc.getString("ListName").toString()
                val workoutplan = doc.getString("Workoutplan").toString()


                itemList.add(MyWorckout_Plna_List_Data(id,imagUrl, Title,videoUrl,CoachId,note,sets,repsets,listname,workoutplan))
                    binding.RecyclerView.adapter = DisplayWorkoutPlan_List_Adapter(itemList)
                    }

            }
        }
    }



