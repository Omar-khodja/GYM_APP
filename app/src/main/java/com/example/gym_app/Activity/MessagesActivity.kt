package com.example.gym_app.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gym_app.Adapter.MessagesAdapter
import com.example.gym_app.Adapter.NewMessageAdapter
import com.example.gym_app.ChatActivityData
import com.example.gym_app.ChatOtherUser
import com.example.gym_app.Login.MainActivity
import com.example.gym_app.MessagesData
import com.example.gym_app.NewMessageData
import com.example.gym_app.R
import com.example.gym_app.User
import com.example.gym_app.databinding.ActivityMessagesBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MessagesActivity : AppCompatActivity() {
    lateinit var biding : ActivityMessagesBinding

    var db = FirebaseFirestore.getInstance()
    val myId = User.UserId
    var itemlist:MutableList<MessagesData> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.title = "Messages"
        setContentView(R.layout.activity_messages)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this,R.layout.activity_messages)
    }



    private fun LestenForMessages() {
        var latestMessages = db.collection("/Latest_Messages/$myId/Latest")

        latestMessages.get()
            .addOnSuccessListener { document->
                for(doc in document){
                    val imgUri = doc.getString("imagUri").toString()
                    val username = doc.getString("username").toString()
                    val latestmessag = doc.getString("mesg").toString()
                    val sender = doc.getString("sender").toString()
                    val id = doc.id
                    val data = MessagesData(
                        id,
                        username,
                        imgUri,
                        latestmessag,
                        sender
                    )
                    itemlist.add(data)
                }
                biding.recyclerView.adapter = MessagesAdapter(this , itemlist){item->
                    Toast.makeText(this, "Clicked item: ${item.userId}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,ChatActivity::class.java)
                    ChatOtherUser.username = item.username
                    ChatOtherUser.otherId = item.userId
                    ChatOtherUser.imguri = item.imagUri
                    startActivity(intent)
                }
                biding.recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL))
                listenForNewMessages()

            }
    }
    private fun listenForNewMessages() {
        val collection = db.collection("/Latest_Messages/$myId/Latest")

        // Listen for new messages only
        collection.addSnapshotListener { value, error ->
            if (error != null) {
                Log.i("tagy", "Error listening for messages: $error")
                return@addSnapshotListener
            }

            value?.documents?.forEach { doc ->
                val docData = doc.data
                val messageId = doc.id
                val imgUri = docData?.get("imagUri").toString()
                val message = docData?.get("mesg").toString()
                val sender = doc.getString("sender").toString()
                val username = doc.getString("username").toString()

                // Check if the document is new
                if (!itemlist.any { it.userId == messageId }) {
                    val chatData = MessagesData(
                        messageId,
                        imgUri,
                        message,
                        sender,
                        username
                    )
                    itemlist.add(chatData)
                }
            }

            // Notify the adapter about the data change
            biding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.newmessages, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle menu item clicks
        when (item.itemId) {
            R.id.newMassages -> {
                startActivity(Intent(this,NewMessageActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onStart() {
        var auth = Firebase.auth
        if(auth.currentUser != null){
            itemlist.clear()
            LestenForMessages()
        }else{
            startActivity(Intent(this , MainActivity::class.java))
        }
        super.onStart()
    }
}