package com.example.gym_app.Activity.Messages

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.ChatOneAdapter
import com.example.gym_app.ChatActivityData
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.Singlton.ChatOtherUser
import com.example.gym_app.LatestMessagesData
import com.example.gym_app.databinding.ActivityChatBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class ChatActivity : AppCompatActivity() {
    lateinit var biding: ActivityChatBinding
    var auth = Firebase.auth
    var db = FirebaseFirestore.getInstance()
    var itemlist: MutableList<ChatActivityData> = mutableListOf()
    var ltestitemlist: MutableList<LatestMessagesData> = mutableListOf()
    val toid = ChatOtherUser.instance?.otherId?.trim()
    val fromid = auth.currentUser?.uid.toString().trim()
    var latestMessages1 = db.collection("/Latest_Messages").document("/$fromid/Latest/$toid")
    var latestMessages2 = db.collection("/Latest_Messages").document("/$toid/Latest/$fromid")





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.title = ChatOtherUser.instance?.username

        setContentView(R.layout.activity_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        var collectionfrom = db.collection("/Messages/$fromid/$toid")
        var collectioninto = db.collection("/Messages/$toid/$fromid")

        listenForMyMessages()
        biding.Senbtn.setOnClickListener {
            var messag = biding.chat.text.toString()
            val id = UUID.randomUUID().toString()
            val timestamp = Timestamp.now().toDate()
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(timestamp)
            Log.i("tagy","$formattedDate")
            var x = ChatActivityData(
                id,
                User.instance?.ProfileimagUri.toString(),
                messag,
                "RIGHT",
                fromid,
                toid,
                formattedDate.toString()
            )
            biding.chat.text.clear()
            collectionfrom.add(x).addOnSuccessListener {
                x = ChatActivityData(
                    id,
                    User.instance?.ProfileimagUri.toString(),
                    messag,
                    "LEFT",
                    fromid,
                    toid,
                    formattedDate.toString()
                )
                collectioninto.add(x)
            }

        }
    }

    private fun listenForMyMessages() {
        val collection = db.collection("/Messages/$fromid/$toid")


        // Initial retrieval of all messages
        collection.orderBy("time").get().addOnSuccessListener { querySnapshot ->
            for (doc in querySnapshot.documents) {
                val messageId = doc.id
                val imgUri = doc.getString("imagUri").toString()
                val message = doc.getString("mesg")
                val sender = doc.getString("sender")
                val toId = doc.getString("toId")
                val fromId = doc.getString("fromId")
                val time = doc.getString("time")

                val chatData = ChatActivityData(
                    messageId,
                    imgUri,
                    message,
                    sender,
                    toId,
                    fromId,
                    time
                )
                itemlist.add(chatData)
            }
            val adapter = ChatOneAdapter(this, itemlist)
            biding.chatRecycleView.adapter = adapter

            // After retrieving initial messages, set up a listener for new messages
            listenForNewMessages()
        }.addOnFailureListener { e ->
            Log.e("tagy", "Error getting documents: ", e)
        }
    }

    private fun listenForNewMessages() {
        val collection = db.collection("/Messages/$fromid/$toid")

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
                val sender = doc.getString("sender")
                val toId = docData?.get("toId").toString()
                val fromId = docData?.get("fromId").toString()
                val time = doc.getString("time").toString()

                // Check if the message is new
                if (!itemlist.any { it.Id == messageId }) {
                    val chatData = ChatActivityData(
                        messageId,
                        imgUri,
                        message,
                        sender,
                        toId,
                        fromId,
                        time
                    )
                    itemlist.add(chatData)
                    val lastchatData = LatestMessagesData(
                        messageId,
                        ChatOtherUser.instance?.imguri.toString(),
                        message,
                        sender,
                        fromId,
                        toId,
                        ChatOtherUser.instance?.username,
                        time
                    )
                    latestMessages1.set(lastchatData).addOnSuccessListener {
                        if (sender == "RIGHT") {
                            val chatData = LatestMessagesData(
                                messageId,
                                User.instance?.ProfileimagUri.toString(),
                                message,
                                "LEFT",
                                toId,
                                fromId,
                                ChatOtherUser.instance?.username,
                                time
                            )
                            latestMessages2.set(chatData)

                        } else {
                            val chatData = LatestMessagesData(
                                messageId,
                                User.instance?.ProfileimagUri.toString(),
                                message,
                                "RIGHT",
                                fromId,
                                toId    ,
                                ChatOtherUser.instance?.username,
                                time
                            )
                            latestMessages2.set(chatData)
                        }
                    }
                }
            }
            val adapter = ChatOneAdapter(this, itemlist)
            biding.chatRecycleView.adapter = adapter
        }

    }

}