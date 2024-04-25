package com.example.gym_app.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.gym_app.Activity.Notification_Activity
import com.example.gym_app.Login.MainActivity
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
     var auth=Firebase.auth
     var db= FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()
    var userId = auth.currentUser?.uid
    var userCollection = db.collection("Users").whereEqualTo("userId", userId)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        //fetchuser
        userCollection.get()
            .addOnSuccessListener { qury ->
                if (!qury.isEmpty) {
                    val document = qury.documents[0] // Assuming there's only one document with the matching user ID
                    binding.ProfileUsername.text = document.getString("username")
                    binding.ProfileEmail.text = document.getString("email")
                    Glide.with(this)
                        .load(Uri.parse(document.getString("ProfileimagUri")))
                        .into(binding.imageView1)
                    binding.phoneNumber.text = document.getString("userphonenb")

                }
            }


        binding.singout.setOnClickListener {
            Toast.makeText(context, "sing out successful", Toast.LENGTH_SHORT).show()
            auth.signOut()
            startActivity(Intent(context, MainActivity::class.java))
        }

        binding.editeImag.setOnClickListener {
            // Create an intent to pick an image from the device
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*" // Allow only images
            startActivityForResult(intent, 1)
        }

        binding.card3.setOnClickListener{
            startActivity(Intent(context,Notification_Activity::class.java))
        }

        return binding.root
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Log.i("tagy","im in onActivityResult")
            val imageUri = data.data
            if (imageUri != null) {
                Log.i("tagy","im in ")
                uploadImageAndSetUserProfile(imageUri)
            } else {
                Toast.makeText(context, "Failed to retrieve image URI", Toast.LENGTH_SHORT).show()
                Log.i("tagy","Failed to retrieve image URI")
            }
        }
    }

    fun uploadImageAndSetUserProfile(imageUri: Uri) {
        val imageName = "${auth.currentUser?.uid}"
        val storageRef = storage.getReference().child("profile_images").child(imageName)

        Log.i("tagy","im in uploadImageAndSetUserProfile")

        storageRef.putFile(imageUri).addOnSuccessListener {
            Log.i("tagy","upload done")
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Log.i("tagy","Download done")
                val profileImageUrl = uri.toString()
                db.collection("Users").document(User.instance?.UserId.toString()).update("ProfileimagUri",profileImageUrl)


            }.addOnFailureListener { e ->
                Toast.makeText(context, "Failed to get download URL: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        if(auth.currentUser ==null){
            startActivity(Intent(context, MainActivity::class.java))
        }else{
            followersNb(){
                binding.followersnb.text = it.toString()
            }
            followingNb() {
                binding.followingnb.text = it.toString()
            }
        }

        super.onStart()
    }
    }





