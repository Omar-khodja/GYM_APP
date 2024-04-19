package com.example.gym_app.Activity.Workout_plan

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.Display_Client_WorkoutPlan_List_Adapter
import com.example.gym_app.MyWorckout_Plna_List_Data
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityDisplayListPlanBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class Display_List_Activity : AppCompatActivity() {
    lateinit var biding : ActivityDisplayListPlanBinding
    lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_list_plan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this , R.layout.activity_display_list_plan)
        db = FirebaseFirestore.getInstance()
        var wourkoutplan = intent.getStringExtra("workout").toString()
        var Title = intent.getStringExtra("Title").toString()
        var planName = intent.getStringExtra("planName").toString()
        var coachId= intent.getStringExtra("CoachId").toString()
        var collection = db.collection("/$wourkoutplan/${User.instance?.UserId}/Workoutplan/$planName/List/$Title/$Title")
        collection.get().addOnSuccessListener {
            featch(it)
        }
        biding.finch.setOnClickListener{
            var collection = db.collection("Client_Progress")
                .document(coachId)
                .collection("Client")
                .document(User.instance?.UserId.toString())
            collection.get().addOnSuccessListener {
                if(it.exists()){
                    var progress = Integer.parseInt(it.getString("progress").toString())
                    var goal = Integer.parseInt(it.getString("goal").toString())
                    if(goal != progress) {
                        progress = progress + 1
                        collection.update("progress", progress.toString())
                        biding.finch.isEnabled = false
                        biding.finch.background = ContextCompat.getDrawable(this,R.drawable.unablebtn)

                    }
                }
            }
        }
    }

    private fun featch(it: QuerySnapshot) {
        val itemlist = mutableListOf<MyWorckout_Plna_List_Data>()
        for (doc in it.documents){
            val id = doc.id
            val imagUrl = doc.getString("ImahUrl").toString()
            val Title = doc.getString("VideoName").toString()
            val videoUrl = doc.getString("VideoUrl").toString()
            val CoachName = doc.getString("CoachId").toString()
            val note = doc.getString("Note").toString()
            val sets = doc.getString("sets").toString()
            val repsets = doc.getString("repsets").toString()
            val listname = doc.getString("ListName").toString()
            val workoutplan = doc.getString("Workoutplan").toString()

            Log.i("tagy",id)
            itemlist.add(MyWorckout_Plna_List_Data(id,imagUrl,Title,videoUrl,CoachName,note,sets,repsets,listname,workoutplan))
            biding.RecyclerView.adapter = Display_Client_WorkoutPlan_List_Adapter(itemlist)


        }    }

}