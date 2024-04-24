package com.example.gym_app.Activity.Workout_plan

import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.ExerciseList_Adapter
import com.example.gym_app.Singlton.DisplayVideo_Singlton
import com.example.gym_app.ExerciseLis_Data
import com.example.gym_app.R
import com.example.gym_app.databinding.ActivityDisplayexercuselistVideosBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DisplayExerciseList_Activity : AppCompatActivity() {
    lateinit var binding: ActivityDisplayexercuselistVideosBinding
    lateinit var db: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var Title: String
    lateinit var path: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_displayexercuselist_videos)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_displayexercuselist_videos)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        path = intent.getStringExtra("path").toString()
        Title = intent.getStringExtra("title").toString()
        supportActionBar?.title = "DisplayExerciseList_Activity"
        val collection = db.collection("Exercise_videos/$path/Exercise")
        val itemList = mutableListOf<ExerciseLis_Data>()

        collection.get().addOnSuccessListener { documentSnapshot ->
            for (doc in documentSnapshot.documents) {
                val title = doc.getString("Title").toString()
                val imagUrl = doc.getString("ImagUrl").toString()
                val des = doc.getString("des").toString()
                val videoUrl = doc.getString("VideoUrl").toString()


                itemList.add(ExerciseLis_Data(imagUrl, title,videoUrl,des))
                    binding.RecyclerView.adapter = ExerciseList_Adapter(this,itemList){item->
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
