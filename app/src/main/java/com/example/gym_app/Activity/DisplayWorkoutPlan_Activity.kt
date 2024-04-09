package com.example.gym_app.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.DisplayWorkoutPolan_Adapter
import com.example.gym_app.MyWorckoutPlna_Data
import com.example.gym_app.R
import com.example.gym_app.User
import com.example.gym_app.databinding.ActivityDisplayWorkoutPlanBinding
import com.google.firebase.firestore.FirebaseFirestore

class DisplayWorkoutPlan_Activity : AppCompatActivity() {
    lateinit var biding : ActivityDisplayWorkoutPlanBinding
    lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_workout_plan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this , R.layout.activity_display_workout_plan)
        db = FirebaseFirestore.getInstance()
        val itemlist = mutableListOf<MyWorckoutPlna_Data>()
        var name = intent.getStringExtra("name")
        var collection = db.collection("/Coach_Workout_Plan/${User.UserId}/Workoutplan/$name/$name")
        collection.get().addOnSuccessListener {
            for (doc in it.documents){
                val imagUrl = doc.getString("ImahUrl").toString()
                val Title = doc.getString("ListName").toString()
                val videoUrl = doc.getString("VideoUrl").toString()
                val des = doc.getString("des").toString()
                val CoachName = doc.getString("CoachName").toString()
                itemlist.add(MyWorckoutPlna_Data(imagUrl,Title,videoUrl,des,CoachName))
                biding.RecyclerView.adapter = DisplayWorkoutPolan_Adapter(itemlist)
            }
        }
    }
}