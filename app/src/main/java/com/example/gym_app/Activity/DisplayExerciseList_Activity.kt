package com.example.gym_app.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.ExerciseList_Adapter
import com.example.gym_app.DisplayVideo_Singlton
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
    lateinit var storageReference: StorageReference
    lateinit var title: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_displayexercuselist_videos)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_displayexercuselist_videos)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        title = intent.getStringExtra("path").toString()
        supportActionBar?.title = title
        val collection = db.collection("Exercise_videos/$title/Exercise")
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
                        DisplayVideo_Singlton.title = item.Title
                        DisplayVideo_Singlton.videoUrl=item.videoUrl
                        DisplayVideo_Singlton.des = item.des
                        DisplayVideo_Singlton.imagUrl = item.imagUrl

                        startActivity(intent)

                    }

            }
        }
    }


}
