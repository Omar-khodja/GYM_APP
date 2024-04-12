package com.example.gym_app.Activity.Workout_plan

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.DisplayWorkoutPolan_Adapter
import com.example.gym_app.MyWorckoutPlna_Data
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityDisplayWorkoutPlanBinding
import com.example.gym_app.databinding.DisplayWorkoutplanBinding
import com.google.firebase.firestore.FirebaseFirestore

class DisplayWorkoutPlan_Activity : AppCompatActivity() {
    lateinit var biding : ActivityDisplayWorkoutPlanBinding
    lateinit var binding : DisplayWorkoutplanBinding
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
        var name = intent.getStringExtra("name").toString()
        var collection = db.collection("/Coach_Workout_Plan/${User.instance?.UserId}/Workoutplan/$name/$name")
        collection.get().addOnSuccessListener {
            for (doc in it.documents){
                val id = doc.id
                val imagUrl = doc.getString("ImahUrl").toString()
                val Title = doc.getString("VideoName").toString()
                val videoUrl = doc.getString("VideoUrl").toString()
                val des = doc.getString("des").toString()
                val CoachName = doc.getString("CoachName").toString()
                val note = doc.getString("Note").toString()
                Log.i("tagy",id)
                itemlist.add(MyWorckoutPlna_Data(id,name,imagUrl,Title,videoUrl,des,CoachName,note))
                biding.RecyclerView.adapter = DisplayWorkoutPolan_Adapter(itemlist)
            }
        }
    }

}