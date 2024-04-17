package com.example.gym_app.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym_app.Activity.Messages.MessagesActivity
import com.example.gym_app.Activity.Search_Activity
import com.example.gym_app.Activity.Workout_plan.Display_Workoutplan_List_Activity
import com.example.gym_app.Adapter.Display_Client_WorkoutPLan_Adapter
import com.example.gym_app.Adapter.WorkoutPlan_Adapter
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.WoroutPlan_Data
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
        db = Firebase.firestore
        var itemlist = mutableListOf<WoroutPlan_Data>()
        val collection =
            db.collection("Client_Workout_Plan")
                .document(User.instance?.UserId.toString())
                .collection("Workoutplan")


        collection.get().addOnSuccessListener{
            Log.i("tagy","im here")
            if (it != null) {
                Log.i("tagy","im here")
                for (document in it.documents) {
                    Log.i("tagy","im here")
                    var Title = document.id
                    itemlist.add(WoroutPlan_Data(Title, "", R.drawable.workout))

                }

                biding.RecyclerView.adapter = Display_Client_WorkoutPLan_Adapter(itemlist) { item ->
                    Log.i("tagy","im here")
                    var intent = Intent(context, Display_Workoutplan_List_Activity::class.java)
                    val title = item.Title
                    val plan = "Client_Workout_Plan"
                    intent.putExtra("name", title)
                    intent.putExtra("plan", plan)
                    startActivity(intent)
                }
            }
        }


    }



}