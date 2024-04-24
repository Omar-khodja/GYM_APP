package com.example.gym_app.Activity.Workout_plan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.ExerciseList_Adapter
import com.example.gym_app.ExerciseLis_Data
import com.example.gym_app.R
import com.example.gym_app.Singlton.DisplayVideo_Singlton
import com.example.gym_app.databinding.ActivityCeateExerciseListBinding
import com.google.firebase.firestore.FirebaseFirestore

class Display_ExerciseList_Video_Activity : AppCompatActivity() {
    lateinit var biding : ActivityCeateExerciseListBinding
    var db = FirebaseFirestore.getInstance()
    lateinit var Title: String
    lateinit var path: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_ceate_exercise_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this,R.layout.activity_ceate_exercise_list)
        path = intent.getStringExtra("path").toString()
        Title = intent.getStringExtra("title").toString()
        supportActionBar?.title = "CeateExerciseList_Activity"
        val collection = db.collection("Exercise_videos/$path/Exercise")
        val itemList = mutableListOf<ExerciseLis_Data>()

        collection.get().addOnSuccessListener { documentSnapshot ->
            for (doc in documentSnapshot.documents) {
                val title = doc.getString("Title").toString()
                val imagUrl = doc.getString("ImagUrl").toString()
                val des = doc.getString("des").toString()
                val videoUrl = doc.getString("VideoUrl").toString()


                itemList.add(ExerciseLis_Data(imagUrl, title,videoUrl,des))
                biding.RecyclerView.adapter = ExerciseList_Adapter(this,itemList){item->
                    var intent = Intent(this , DisplayVideos_Activity::class.java)
                    DisplayVideo_Singlton.instance?.title = item.Title
                    DisplayVideo_Singlton.instance?.videoUrl=item.videoUrl
                    DisplayVideo_Singlton.instance?.des = item.des
                    DisplayVideo_Singlton.instance?.imagUrl = item.imagUrl
                    intent.putExtra("title",Title)
                    startActivity(intent)

                }

            }
        }

    }
}