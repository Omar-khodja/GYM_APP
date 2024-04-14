package com.example.gym_app.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.Notification_Adapter
import com.example.gym_app.ChatActivityData
import com.example.gym_app.NewMessageData
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityNotificationBinding
import com.google.firebase.firestore.FirebaseFirestore

class Notification_Activity : AppCompatActivity() {
    lateinit var biding :ActivityNotificationBinding
    var db = FirebaseFirestore.getInstance()
    var itemlist = mutableListOf<NewMessageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this,R.layout.activity_notification)
        listen()


        biding.toolbar2.title = "Notification"

    }

    private fun getnotification() {
        var collection = db.collection("Follow_request")
            .document(User.instance?.UserId.toString())
            .collection("Followers")
        collection.get().addOnSuccessListener {
            for(doc in it.documents){
                var id = doc.getString("UserId").toString()
                var username = doc.getString("UserName").toString()
                var img = doc.getString("ProfileimagUri").toString()
                itemlist.add(NewMessageData(id,username,img))
            }
            biding.RecyclerView.adapter = Notification_Adapter(itemlist)

        }

    }
    private fun listen() {
        var collection = db.collection("Follow_request")
            .document(User.instance?.UserId.toString())
            .collection("Followers")
        collection.addSnapshotListener { value, error ->
            if (error != null) {
                Log.i("tagy", "Error listening for messages: $error")
                return@addSnapshotListener
            }
            var i =0

            value?.documents?.forEach { doc ->
                val id = doc.id
                var username = doc.getString("UserName").toString()
                var img = doc.getString("ProfileimagUri").toString()

                // Check if the message is new
                if (!itemlist.any { it.userId == id }) {
                    itemlist.add(NewMessageData(id,username,img))
                }else if (itemlist.any { it.userId == id }){
                    itemlist.removeAt(i)
                }
                i++

            }
            biding.RecyclerView.adapter = Notification_Adapter(itemlist)

        }
    }
}