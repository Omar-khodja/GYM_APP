package com.example.gym_app.Activity.Workout_plan

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Singlton.DisplayVideo_Singlton
import com.example.gym_app.R
import com.example.gym_app.Singlton.TrueOrFalse
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityDisplayVideosBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class DisplayVideos_Activity : AppCompatActivity() {
    lateinit var biding : ActivityDisplayVideosBinding
    lateinit var storageReference:StorageReference
    lateinit var storage:FirebaseStorage
     var db=  FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_videos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this ,R.layout.activity_display_videos)
        storage=FirebaseStorage.getInstance()
        storageReference=storage.getReference()
        val videoUrl = Uri.parse(DisplayVideo_Singlton.instance?.videoUrl)
        val des = DisplayVideo_Singlton.instance?.des
        val Title = DisplayVideo_Singlton.instance?.title
        val filename = "video_${title}.mp4"
        val localFile = File(filesDir, filename)
        storageReference = storage.getReferenceFromUrl(videoUrl.toString())
        storageReference.getFile(localFile)
            .addOnSuccessListener {
                Log.d("tagy", "Video downloaded: $filename")
                biding.VideoPlayer.setVideoPath(localFile.absolutePath)
                biding.VideoPlayer.setOnPreparedListener{
                    it.isLooping =true
                    biding.VideoPlayer.start()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("tagy", "Failed to download video: $filename", exception)
                // Handle download failure here
            }
        biding.des.text=des
        supportActionBar?.title=Title
        if(TrueOrFalse.instance?.boolean == true){
            biding.addbtn.visibility = View.VISIBLE
        }
        biding.addbtn.setOnClickListener{
            var title = intent.getStringExtra("title")
            AddVideoToPlane(title)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun AddVideoToPlane(title: String?) {
        val name = TrueOrFalse.instance?.name.toString()

        var collection = db.collection("/Coach_Workout_Plan")
                                .document(User.instance?.UserId.toString())
                                .collection("Workoutplan")
                                .document(name)
                                .collection("List")
                                .document(title.toString())
                                .collection(title.toString())
        var collection1 = db.collection("/Coach_Workout_Plan")
            .document(User.instance?.UserId.toString())
            .collection("Workoutplan")
            .document(name)
            .collection("List")
            .document(title.toString())




        var videoUrl = DisplayVideo_Singlton.instance?.videoUrl
        var data = hashMapOf(
            "Workoutplan" to name,
            "ListName" to title.toString(),
            "CoachName" to User.instance?.UserName,
            "CoachId" to User.instance?.UserId,
            "VideoUrl" to videoUrl,
            "ImahUrl" to DisplayVideo_Singlton.instance?.imagUrl,
            "des" to DisplayVideo_Singlton.instance?.des,
            "VideoName" to DisplayVideo_Singlton.instance?.title,
            "Note" to ""
        )
        var data1 = hashMapOf(
            "Title" to title.toString(),
            "Workoutplan" to name
        )
        collection1.set(data1).addOnSuccessListener {
            collection.add(data).addOnSuccessListener {
                Toast.makeText(this,"video add to ${title.toString()}",Toast.LENGTH_SHORT).show()
            }
        }


    }
}