package com.example.gym_app.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gym_app.Activity.Messages.ChatActivity
import com.example.gym_app.Adapter.NewMessageAdapter
import com.example.gym_app.Singlton.ChatOtherUser
import com.example.gym_app.NewMessageData
import com.example.gym_app.R
import com.example.gym_app.databinding.ActivitySearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Search_Activity : AppCompatActivity() {
    lateinit var biding : ActivitySearchBinding
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        var collection = db.collection("Users")
        fetchusers(collection)
        biding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            true
        }
    }

    private fun search() {
        var text = biding.editText.text.toString().trim()
        if (text.isNotEmpty()) {
            var collection = db.collection("Users").whereEqualTo("username", text)
          fetchusers(collection)
        }
    }

    private fun fetchusers(collection: Query) {
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
                biding.RecyclerView.adapter = NewMessageAdapter( adapter){item->
                    Toast.makeText(this, "Clicked item: ${item.username}", Toast.LENGTH_SHORT).show()
                    var intent= Intent(this , ChatActivity::class.java)
                    ChatOtherUser.instance?.username = item.username
                    ChatOtherUser.instance?.otherId = item.userId
                    ChatOtherUser.instance?.imguri = item.imguri
                    startActivity(intent)
                }
                biding.RecyclerView.addItemDecoration(
                    DividerItemDecoration(this,
                        DividerItemDecoration.VERTICAL)
                )

            }
    }
}