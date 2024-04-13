package com.example.gym_app.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.example.gym_app.Activity.Workout_plan.Create_Workoutplan_Activity
import com.example.gym_app.Activity.Workout_plan.DisplayWorkoutPlan_Activity
import com.example.gym_app.Adapter.WorkoutPlan_Adapter
import com.example.gym_app.R
import com.example.gym_app.Singlton.TrueOrFalse
import com.example.gym_app.Singlton.User
import com.example.gym_app.WoroutPlanList_Data
import com.example.gym_app.databinding.FragmentCtreateworoutplaneBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class CreateWorkoutPlane_Fragment : Fragment() {
    lateinit var binding: FragmentCtreateworoutplaneBinding
     lateinit var db:FirebaseFirestore
     lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCtreateworoutplaneBinding.inflate(inflater, container, false)
        binding.floatingActionButton.setOnClickListener{
            context?.let { it1 -> showInputDialog(it1) }
        }
        auth = Firebase.auth
        collections()

        return binding.root
    }

    private fun collections() {
        db = Firebase.firestore
        var itemlist = mutableListOf<WoroutPlanList_Data>()
        val collection = db.collection("Coach_Workout_Plan").document(User.instance?.UserId.toString()).collection("Workoutplan")

        var Title = ""
        collection.get().addOnSuccessListener { result ->
            for (document in result) {
                Title = document.id
                itemlist.add(WoroutPlanList_Data(Title, R.drawable.workout))

            }
            binding.RecyclerView.adapter = WorkoutPlan_Adapter(itemlist){item ->
                var intent = Intent(context, DisplayWorkoutPlan_Activity::class.java)
                val title = item.Title
                intent.putExtra("name",title)
                startActivity(intent)
            }
            binding.textView.visibility = View.INVISIBLE
        }
        binding.appbar.title = "Create Worckout Plan"




    }

    fun showInputDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Enter Your Workout Plan Name")

        val inputLayout = LinearLayout(context)
        inputLayout.setPadding(50, 0, 50, 0)
        val input = EditText(context)
        inputLayout.addView(input)
        input.width = 400
        alertDialog.setView(inputLayout)

        alertDialog.setPositiveButton("OK") { dialog, _ ->
            val enteredText = input.text.toString()
            // Handle the entered text here
            var intent = Intent(context, Create_Workoutplan_Activity::class.java)
            TrueOrFalse.instance?.boolean = true
            TrueOrFalse.instance?.name = enteredText
            startActivity(intent)
            dialog.dismiss()
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.show()
    }

    override fun onPause() {
        db = Firebase.firestore
        var itemlist = mutableListOf<WoroutPlanList_Data>()
        val collection = db.collection("Coach_Workout_Plan")
            .document(User.instance?.UserId.toString())
            .collection("Workoutplan")

        var Title = ""
        collection.get().addOnSuccessListener { result ->
            for (document in result.documents) {
                Title = document.id
                itemlist.add(WoroutPlanList_Data(Title, R.drawable.workout))

            }
            binding.RecyclerView.adapter = WorkoutPlan_Adapter(itemlist){item ->
                var intent = Intent(context, DisplayWorkoutPlan_Activity::class.java)
                val title = item.Title
                intent.putExtra("name",title)
                startActivity(intent)
            }
            binding.textView.visibility = View.INVISIBLE
        }
        binding.appbar.title = "Create Worckout Plan"



        super.onPause()
    }
   
}






