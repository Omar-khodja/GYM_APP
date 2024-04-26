package com.example.gym_app.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityCoachProfileCustomizationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class coachProfileCustomizationActivity : AppCompatActivity() {
    lateinit var bindaing : ActivityCoachProfileCustomizationBinding
    var db = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_coach_profile_customization)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bindaing = DataBindingUtil.setContentView(this,
            R.layout.activity_coach_profile_customization
        )
        fretchUser()
        bindaing.bioTextView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                db.collection("Users")
                    .document(User.instance?.UserId.toString())
                    .update("Bio",bindaing.bioTextView.text.toString())
                Log.i("tagy","updat success ${bindaing.bioTextView.text}")
            }
            true
        }

        bindaing.editPFPReview.setOnClickListener{
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,1)

        }
        bindaing.AboutVideoPlayerBTN.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 2)
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                uploadImageAndSetUserProfile(imageUri)
            } else {
                Toast.makeText(this, "Failed to retrieve image URI", Toast.LENGTH_SHORT).show()
                Log.i("tagy","Failed to retrieve image URI")
            }
        }else if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            val videoUri = data.data!!
            if(videoUri != null){
                uploadVideoUri(videoUri)

            }
        }
    }

    private fun uploadVideoUri(videoUri: Uri) {
        val videoName = "${User.instance?.UserId}"
        val storageRef = storage.getReference().child("profile_video").child(videoName)
        storageRef.putFile(videoUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                bindaing.AboutVideoPlayer.setVideoURI(it)
                db.collection("Users")
                    .document(User.instance?.UserId.toString())
                    .update("VideoUri",it.toString())


            }
        }

    }

    private fun uploadImageAndSetUserProfile(imageUri: Uri) {
        val imageName = "${User.instance?.UserId}"
        val storageRef = storage.getReference().child("profile_images").child(imageName)


        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(this)
                    .load(uri)
                    .into(bindaing.coachPFP)
                val profileImageUrl = uri.toString()
                db.collection("Users").document(User.instance?.UserId.toString()).update("ProfileimagUri",profileImageUrl)


            }.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to get download URL: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fretchUser() {
        //set imag and name
        Glide.with(this)
            .load(User.instance?.ProfileimagUri)
            .into(bindaing.coachPFP)
        bindaing.coachNametextView.text = User.instance?.UserName

        //set followers and following nb
        followersNb(){
            bindaing.followersCount.text = it.toString()
        }
        followingNb() {
            bindaing.followingCount.text = it.toString()
        }
        //set bio
        bindaing.bioTextView.setText(User.instance?.Bio)
        if(User.instance?.VideoUri.toString().isNotEmpty()) {
            downloadAndPlayVideo(User.instance?.VideoUri.toString())
        }



    }
    private fun downloadAndPlayVideo(videoUrl: String) {
        val storageRef = storage.getReferenceFromUrl(videoUrl)

        val localFile = File.createTempFile("video", "mp4")
        storageRef.getFile(localFile)
            .addOnSuccessListener {
                // Video downloaded successfully, now play it in VideoView
                playVideo(localFile.absolutePath)
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(this, "Failed to download video: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun playVideo(videoPath: String) {
        // Set up media controller for video playback controls
        val mediaController = MediaController(this)
        mediaController.setAnchorView(bindaing.AboutVideoPlayer)
        bindaing.AboutVideoPlayer.setMediaController(mediaController)

        // Set video path and start playback
        bindaing.AboutVideoPlayer.setVideoPath(videoPath)
        bindaing.AboutVideoPlayer.start()
    }
    private fun followersNb( callback: (Int) -> Unit) {
        var collection = db.collection("Followers")
            .document(User.instance?.UserId.toString())
            .collection("Followers")

        var i = 0 // Initialize i to 0

        collection.get().addOnSuccessListener {
            for (doc in it.documents) {
                i++
            }
            callback(i) // Pass the value of i to the callback function
        }
    }
    private fun followingNb(callback: (Int) -> Unit) {
        var collection = db.collection("Following")
            .document(User.instance?.UserId.toString())
            .collection("Following")

        var i = 0 // Initialize i to 0

        collection.get().addOnSuccessListener {
            for (doc in it.documents) {
                i++
            }
            callback(i) // Pass the value of i to the callback function
        }

    }
}