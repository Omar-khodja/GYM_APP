package com.example.gym_app.Activity.Workout_plan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.Display_Workoutplan_ListTiming_Adapter
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.WoroutPlan_Data
import com.example.gym_app.databinding.ActivityDisplayWorkoutplanListBinding
import com.google.firebase.firestore.FirebaseFirestore

class Display_Workoutplan_List_Activity : AppCompatActivity() {
    lateinit var biding:ActivityDisplayWorkoutplanListBinding
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_display_workoutplan_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this,R.layout.activity_display_workoutplan_list)
        val title = intent.getStringExtra("name").toString()
        val plan = intent.getStringExtra("plan").toString()
        displaylist(title , plan)
        biding.toolbar.title= title
    }

    private fun displaylist(title: String, plan: String) {
        var itemlist = mutableListOf<WoroutPlan_Data>()
        var collection = db.collection(plan)
            .document(User.instance?.UserId.toString())
            .collection("Workoutplan")
            .document(title)
            .collection("List")
        collection.get().addOnSuccessListener {
            for(doc in it.documents){
                val Title = doc.id
                val workoutplanName = doc.getString("Workoutplan").toString()
                if(!itemlist.any{it.Title == Title}){
                    itemlist.add(WoroutPlan_Data(Title,workoutplanName, 0))

                }
                biding.RecyclerView.adapter = Display_Workoutplan_ListTiming_Adapter(itemlist) {
                    var intent = Intent(this,Display_List_Activity::class.java)
                    var title1 = it.Title
                    var title2= it.wourkoutplanName
                    var title3 = plan
                    intent.putExtra("Title",title1)
                    intent.putExtra("planName",title2)
                    intent.putExtra("workout",title3)
                    startActivity(intent)

                }



            }
        }
    }
}