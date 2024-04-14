package com.example.gym_app.Activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gym_app.Adapter.Share_Adapter
import com.example.gym_app.NewMessageData
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityShareBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Share_Activity : AppCompatActivity() {
    lateinit var biding:ActivityShareBinding
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_share)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this,R.layout.activity_share)
        var collection = db.collection("Followers")
            .document(User.instance?.UserId.toString())
            .collection("Followers")
        var title = intent.getStringExtra("Title").toString()
        var wourkoutplan = intent.getStringExtra("wourkoutplan").toString()
        fechUsers(collection,title,wourkoutplan)
    }

    private fun fechUsers(collection: Query, title: String, wourkoutplan: String) {
        collection.get()
            .addOnSuccessListener {
                val itemlist: MutableList<NewMessageData> = mutableListOf()
                for(doucment in it){
                    Log.i("tagy","$doucment")
                    itemlist.add(
                        NewMessageData(
                            doucment.getString("UserId").toString()
                            ,doucment.getString("UserName").toString()
                            ,doucment.getString("imagUri").toString())
                    )
                }
                biding.recyclerView2.adapter = Share_Adapter( title,wourkoutplan,itemlist){item->

                }
                biding.recyclerView2.addItemDecoration(
                    DividerItemDecoration(this,
                        DividerItemDecoration.VERTICAL)
                )

            }    }
}