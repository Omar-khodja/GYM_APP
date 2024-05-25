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
import com.example.gym_app.WoroutPlan_Data
import com.example.gym_app.databinding.FragmentCtreateworoutplaneBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class CreateWorkoutPlane_Fragment : Fragment() {
    lateinit var binding: FragmentCtreateworoutplaneBinding
    var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCtreateworoutplaneBinding.inflate(inflater, container, false)
        binding.floatingActionButton.setOnClickListener {
            context?.let { it1 -> showInputDialog(it1) }
        }
        collections()
        toolbar()

        return binding.root
    }

    private fun collections() {

        var itemlist = mutableListOf<WoroutPlan_Data>()
        val collection =
            db.collection("Coach_Workout_Plan")
                .document(User.instance?.UserId.toString())
                .collection("Workoutplan")

        collection.addSnapshotListener{ value, error ->
            if(error!=null){
                Log.i("tagy", "Error listening for messages: $error")
                return@addSnapshotListener
            }
            itemlist.clear()

            if (value != null) {
                for (document in value.documents) {
                    var Title = document.id
                    var coachid = document.getString("CiachId").toString()

                    itemlist.add(WoroutPlan_Data(coachid,Title,"", R.drawable.workout))

                }
            }
            binding.RecyclerView.adapter = WorkoutPlan_Adapter(itemlist) { item ->
                var intent = Intent(context, Create_WorkoutPlanList_Activity::class.java)
                TrueOrFalse.instance?.boolean = true
                TrueOrFalse.instance?.name = item.Title
                startActivity(intent)
            }
            binding.textView.visibility = View.INVISIBLE
        }


    }
    private fun toolbar() {
        binding.appbar.apply {
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
            var intent = Intent(context, Create_WorkoutPlanList_Activity::class.java)
            TrueOrFalse.instance?.boolean = true
            TrueOrFalse.instance?.name = enteredText
            var collection = db.collection("Coach_Workout_Plan")
                .document(User.instance?.UserId.toString())
                .collection("Workoutplan")
                .document(enteredText)
            var data= hashMapOf(
                "Title" to enteredText,
                "CiachId" to User.instance?.UserId.toString()
            )
            collection.set(data)
            startActivity(intent)
            dialog.dismiss()

        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.show()
    }



}






