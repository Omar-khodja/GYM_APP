package com.example.gym_app.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.example.gym_app.Activity.Messages.MessagesActivity
import com.example.gym_app.Activity.Search_Activity
import com.example.gym_app.Activity.Workout_plan.Create_WorkoutPlanList_Activity
import com.example.gym_app.Activity.Workout_plan.Display_Workoutplan_List_Activity
import com.example.gym_app.Adapter.WorkoutPlan_Adapter
import com.example.gym_app.R
import com.example.gym_app.Singlton.TrueOrFalse
import com.example.gym_app.Singlton.User
import com.example.gym_app.WoroutPlanList_Data
import com.example.gym_app.databinding.FragmentClientWorkoutplanBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class Client_Workoutplan_Fragment : Fragment() {
    lateinit var biding :FragmentClientWorkoutplanBinding
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        biding = FragmentClientWorkoutplanBinding.inflate(inflater,container,false)
        biding.toolbar.title = "Home"

        collections()
        toolbar()


        return biding.root
    }

    private fun toolbar() {
        biding.toolbar.apply {
            inflateMenu(R.menu.messages_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.message -> {
                        startActivity(Intent(context, MessagesActivity::class.java))
                        true
                    }
                    R.id.Search -> {
                        startActivity(Intent(context, Search_Activity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun collections() {
        Log.i("tagy","yes im here")
        var itemlist = mutableListOf<WoroutPlanList_Data>()
        var collection:CollectionReference = db.collection("Client_Workout_Plan")
                .document(User.instance?.UserId.toString())
                .collection("Workoutplan")


        collection.get().addOnSuccessListener {
            Log.i("tagy","yes im in")
            for(doc in it.documents) {
                var Title = doc.id
                Log.i("tagy", Title)
                itemlist.add(WoroutPlanList_Data(Title,"", R.drawable.workout))
                biding.RecyclerView.adapter = WorkoutPlan_Adapter(itemlist) { item ->
                    var intent = Intent(context, Display_Workoutplan_List_Activity::class.java)
                    val title = item.Title
                    val plan = "Client_Workout_Plan"
                    intent.putExtra("name", title)
                    intent.putExtra("plan", plan)
                    startActivity(intent)
                }
            }
            Log.i("tagy", "here")


        }


    }



}