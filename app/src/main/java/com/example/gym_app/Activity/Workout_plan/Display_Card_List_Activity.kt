package com.example.gym_app.Activity.Workout_plan

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
import com.example.gym_app.Singlton.TrueOrFalse
import com.example.gym_app.databinding.ActivityDisplayCoachexerciselistVideoBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class Display_Card_List_Activity : AppCompatActivity() {
    lateinit var biding:ActivityDisplayCoachexerciselistVideoBinding
    val db = FirebaseFirestore.getInstance()
    val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_coachexerciselist_video)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this , R.layout.activity_display_coachexerciselist_video)
        var Title = intent.getStringExtra("title").toString()
        val itemlis:MutableList<CardView_CreateData> = mutableListOf()
        itemlis.add(CardView_CreateData(R.drawable.abs,"Abs","Abs Exersice","9 Videos"))
        itemlis.add(CardView_CreateData(R.drawable.back,"Back","Back Exersice","9 Videos"))
        itemlis.add(CardView_CreateData(R.drawable.biceps,"Biceps","Biceps Exersice","7 Videos"))
        itemlis.add(CardView_CreateData(R.drawable.calf,"Calf","Calf Exersice","6 Videos"))
        itemlis.add(CardView_CreateData(R.drawable.chest,"Chest","Chest Exersice","8 Videos"))


        biding.createRecyclerView.adapter = CardView_CreateAdapter(itemlis){item->
            val intent = Intent(this, Display_ExerciseList_Video_Activity::class.java)
            intent.putExtra("path",item.title)
            intent.putExtra("title",Title)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        TrueOrFalse.instance?.boolean = false
        super.onDestroy()
    }
}