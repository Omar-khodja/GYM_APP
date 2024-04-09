package com.example.gym_app.Activity.Messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat

import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gym_app.Adapter.NewMessageAdapter
import com.example.gym_app.NewMessageData
import com.example.gym_app.R
import com.example.gym_app.ChatOtherUser
import com.example.gym_app.databinding.ActivityNewMessage2Binding
import com.google.firebase.firestore.FirebaseFirestore

class NewMessageActivity : AppCompatActivity() {
    lateinit var biding : ActivityNewMessage2Binding
     var db = FirebaseFirestore.getInstance()
     val collection = db.collection("Users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.title = "Select User"
        setContentView(R.layout.activity_new_message2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this , R.layout.activity_new_message2)

        fetchusers()
    }

    private fun fetchusers() {
        collection.get()
            .addOnSuccessListener {
                val adapter: MutableList<NewMessageData> = mutableListOf()
                for(doucment in it){
                    Log.i("tagy","$doucment")
                    adapter.add(
                        NewMessageData(
                        doucment.getString("userId").toString()
                        ,doucment.getString("username").toString()
                        ,doucment.getString("ProfileimagUri").toString())
                    )
                }
                biding.recyclerViewNewMessage.adapter = NewMessageAdapter(this , adapter){item->
                    Toast.makeText(this, "Clicked item: ${item.username}", Toast.LENGTH_SHORT).show()
                    var intent=Intent(this , ChatActivity::class.java)
                   ChatOtherUser.username = item.username
                    ChatOtherUser.otherId = item.userId
                    ChatOtherUser.imguri = item.imguri
                    startActivity(intent)
                    finish()//we finish with this Activity so go back to messages activity
                }
                biding.recyclerViewNewMessage.addItemDecoration(
                    DividerItemDecoration(this,
                        DividerItemDecoration.VERTICAL)
                )

            }
    }
}