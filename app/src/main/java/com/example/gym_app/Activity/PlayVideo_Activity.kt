package com.example.gym_app.Activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.R
import com.example.gym_app.databinding.ActivityPlayVideoBinding

class PlayVideo_Activity : AppCompatActivity() {
    lateinit var biding : ActivityPlayVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_play_video)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this , R.layout.activity_play_video)
        var video = intent.getStringExtra("videUrl")
        biding.videoView.setVideoURI(Uri.parse(video))
        biding.videoView.setOnPreparedListener{
            it.isLooping =true
            biding.videoView.start()
        }


    }
}