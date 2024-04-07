package com.example.gym_app.Activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.gym_app.DisplayVideo_Singlton
import com.example.gym_app.R
import com.example.gym_app.databinding.ActivityDisplayVideosBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class DisplayVideos_Activity : AppCompatActivity() {
    lateinit var biding : ActivityDisplayVideosBinding
    lateinit var storageReference:StorageReference
    lateinit var storage:FirebaseStorage

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
        val videoUrl = Uri.parse(DisplayVideo_Singlton.videoUrl)
        val des = DisplayVideo_Singlton.des
        val Title = DisplayVideo_Singlton.title
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

    }
}