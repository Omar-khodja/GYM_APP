package com.example.gym_app.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.CardView_CreateAdapter
import com.example.gym_app.CardView_CreateData
import com.example.gym_app.R
import com.example.gym_app.TrueOrFalse
import com.example.gym_app.databinding.ActivityCreateVideoBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Create_video_Activity : AppCompatActivity() {
    lateinit var biding:ActivityCreateVideoBinding
    val db = FirebaseFirestore.getInstance()
    val auth = Firebase.auth
    val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_video)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this , R.layout.activity_create_video)
        val itemlis:MutableList<CardView_CreateData> = mutableListOf()
        itemlis.add(CardView_CreateData(R.drawable.abs,"Abs","Abs Exersice","9 Videos"))
        itemlis.add(CardView_CreateData(R.drawable.back,"Back","Back Exersice","4 Videos"))

        biding.createRecyclerView.adapter = CardView_CreateAdapter(itemlis){item->
            val intent = Intent(this,DisplayExerciseList_Activity::class.java)
            intent.putExtra("path",item.title)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        TrueOrFalse.boolean = false
        super.onDestroy()
    }
}