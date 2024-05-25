package com.example.gym_app.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.gym_app.Singlton.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UploadService : JobIntentService(){
    companion object {
        private const val JOB_ID = 1000

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, UploadService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val videoUri: Uri? = intent.getParcelableExtra("VIDEO_URI")
        if (videoUri != null) {
            uploadVideoUri(videoUri)
        }
    }

    private fun uploadVideoUri(videoUri: Uri) {
        val videoName = "${User.instance?.UserId}"
        val storage = FirebaseStorage.getInstance()
        val db = FirebaseFirestore.getInstance()
        val storageRef = storage.reference.child("profile_video").child(videoName)

        storageRef.putFile(videoUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                User.instance?.VideoUri = uri
                db.collection("Users").document(User.instance?.UserId.toString())
                    .update("VideoUri", uri.toString())
                    .addOnSuccessListener {

                        Log.d("tagy", "Video successfully uploaded and URI updated")
                    }
                    .addOnFailureListener { e ->
                        Log.e("tagy", "Error updating video URI", e)
                    }
            }.addOnFailureListener { e ->
                Log.e("tagy", "Error getting download URL", e)
            }
        }.addOnFailureListener { e ->
            Log.e("tagy", "Error uploading video", e)
        }
    }
}