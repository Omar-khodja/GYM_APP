package com.example.gym_app.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.gym_app.Activity.Messages.ChatActivity
import com.example.gym_app.R
import com.example.gym_app.Singlton.ChatOtherUser
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityClientProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

class Client_Profile_Activity : AppCompatActivity() {
    lateinit var biding : ActivityClientProfileBinding
     var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_client_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this, R.layout.activity_client_profile)
        var id = intent.getStringExtra("id").toString()
        var img = intent.getStringExtra("img").toString()
        var username = intent.getStringExtra("username").toString()
        fetchUser(id)
        buttonenable(id)
        butonfollowing(id)
        biding.followbtn.setOnClickListener {
            addFollow(id)
        }
        biding.messagebtn.setOnClickListener{
            var intent= Intent(this , ChatActivity::class.java)
            ChatOtherUser.instance?.username = username
            ChatOtherUser.instance?.otherId = id
            ChatOtherUser.instance?.imguri = img
            startActivity(intent)
        }
          followersNb(id){
              biding.followersnb.text = it.toString()
         }
        }

    private fun followersNb(id: String, callback: (Int) -> Unit) {
        var collection = db.collection("Followers")
            .document(id)
            .collection("Followers")

        var i = 0 // Initialize i to 0

        collection.get().addOnSuccessListener {
            for (doc in it.documents) {
                i++
            }
            callback(i) // Pass the value of i to the callback function
        }
    }

    private fun butonfollowing(id: String) {
        var collection =  db.collection("Followers").document(id)
            .collection("Followers")
        collection.addSnapshotListener { value, error ->
            if (error != null) {
                Log.i("tagy", "Error listening for messages: $error")
                return@addSnapshotListener
            }

            value?.documents?.forEach { doc ->

                val id = doc.id


                // Check if im in his followers
                if (id == User.instance?.UserId) {
                    biding.followbtn.background =
                        ContextCompat.getDrawable(biding.root.context, R.drawable.unablebtn)
                    biding.followbtn.text = "Following"
                    biding.followbtn.isEnabled = false
                }
            }


        }
    }

    private fun addFollow(id: String) {
        var collection = db.collection("Follow_request").document(id)
            .collection("Followers").document(User.instance?.UserId.toString())
            var data = hashMapOf(
                "UserId" to User.instance?.UserId,
                "Email" to User.instance?.Email,
                "UserName" to User.instance?.UserName,
                "UserPhonenb" to User.instance?.UserPhonenb,
                "CoachOrClient" to User.instance?.CoachOrClient,
                "ProfileimagUri" to User.instance?.ProfileimagUri
            )
            collection.set(data).addOnSuccessListener {
                Toast.makeText(this,"Request sent",Toast.LENGTH_SHORT).show()
            }
    }


    private fun fetchUser(id: String) {
        var collection = db.collection("Users").document(id)
        collection.get().addOnSuccessListener {
            if(it.exists()){
                Glide.with(this)
                    .load(Uri.parse(it.getString("ProfileimagUri")))
                    .into(biding.PrifileImg)
                supportActionBar?.title = it.getString("username")
                biding.email.text = it.getString("email")
                biding.phoneNumber.text = it.getString("userphonenb")
            }
        }

    }
    private fun buttonenable(userId: String) {
        var collection =  db.collection("Follow_request").document(userId)
            .collection("Followers")
        collection.addSnapshotListener { value, error ->
            if (error != null) {
                Log.i("tagy", "Error listening for messages: $error")
                return@addSnapshotListener
            }

            value?.documents?.forEach { doc ->

                val id = doc.id

                // Check if the message is new
                if (id == User.instance?.UserId) {
                    biding.followbtn.background =
                        ContextCompat.getDrawable(biding.root.context, R.drawable.unablebtn)
                    biding.followbtn.text = "Requested"
                    biding.followbtn.isEnabled = false
                }
            }


        }
    }
}