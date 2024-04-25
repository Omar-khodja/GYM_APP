package com.example.gym_app.Activity.Workout_plan

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Adapter.Display_Workoutplan_ListTiming_Adapter
import com.example.gym_app.R
import com.example.gym_app.Singlton.TrueOrFalse
import com.example.gym_app.Singlton.User
import com.example.gym_app.WoroutPlan_Data
import com.example.gym_app.databinding.ActivityCreateWorkoutPlanListBinding
import com.google.firebase.firestore.FirebaseFirestore

class Create_WorkoutPlanList_Activity : AppCompatActivity() {
    lateinit var biding :ActivityCreateWorkoutPlanListBinding
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_workout_plan_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this,R.layout.activity_create_workout_plan_list)
        var Title = TrueOrFalse.instance?.name.toString()
        biding.toolbar.title =Title


        //add list btn
        biding.floatingActionButton.setOnClickListener{
            showInputDialog(this)
        }
    }
    fun showInputDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Enter Your List  Name")

        val inputLayout = LinearLayout(context)
        inputLayout.setPadding(50, 0, 50, 0)
        val input = EditText(context)
        inputLayout.addView(input)
        input.width = 400
        alertDialog.setView(inputLayout)

        alertDialog.setPositiveButton("OK") { dialog, _ ->
            val enteredText = input.text.toString()
            // Handle the entered text here
            var intent = Intent(this,Display_Card_List_Activity::class.java)
            TrueOrFalse.instance?.boolean = true
            intent.putExtra("title",enteredText )
            startActivity(intent)
            dialog.dismiss()
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        var itemlist = mutableListOf<WoroutPlan_Data>()
        var Title = TrueOrFalse.instance?.name.toString()
        var collection = db.collection("Coach_Workout_Plan")
            .document(User.instance?.UserId.toString())
            .collection("Workoutplan")
            .document(Title)
            .collection("List")
        collection.addSnapshotListener { value, error ->
            if (error != null) {
                Log.i("tagy", "Error listening for messages: $error")
                return@addSnapshotListener
            }
            value?.documents?.forEach { doc ->
                val Title = doc.id
                val workoutplan = doc.getString("Workoutplan").toString()
                if(!itemlist.any{it.Title == Title}){
                itemlist.add(WoroutPlan_Data("",Title,workoutplan, R.drawable.workout))

                }
                biding.RecyclerView.adapter = Display_Workoutplan_ListTiming_Adapter(itemlist) {
                    var intent = Intent(this,Display_Coach_ExerciseList_Activity::class.java)
                    var title1 = it.Title
                    var title2= it.wourkoutplanName
                    intent.putExtra("Title",title1)
                    intent.putExtra("planName",title2)
                    startActivity(intent)

                }



            }
        }
    }
}