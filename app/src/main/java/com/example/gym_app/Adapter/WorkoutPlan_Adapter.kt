package com.example.gym_app.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.Singlton.User
import com.example.gym_app.WoroutPlanList_Data
import com.example.gym_app.databinding.CustomWorkoutplanBinding
import com.google.firebase.firestore.FirebaseFirestore

class WorkoutPlan_Adapter(val itemlist:MutableList<WoroutPlanList_Data>, val OnClik : (WoroutPlanList_Data)-> Unit)
    :RecyclerView.Adapter<WorkoutPlan_Adapter.ViewHolder>(){
           lateinit var biding : CustomWorkoutplanBinding


        class ViewHolder(var biding : CustomWorkoutplanBinding):RecyclerView.ViewHolder(biding.root){
            fun bind(data: WoroutPlanList_Data){
                biding.data = data
                Glide.with(biding.root)
                    .load(data.imagUrl)
                    .into(biding.imageView)
            biding.delete.setOnClickListener{
                showInputDialog(biding.root.context,data.Title)


            }
            }
            fun showInputDialog(context: Context, title: String) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Sure you wante to delete this collection")

                val inputLayout = LinearLayout(context)
                inputLayout.setPadding(50, 0, 50, 0)
                alertDialog.setPositiveButton("yes") { dialog, _ ->
                    var db = FirebaseFirestore.getInstance()
                    db.collection("Coach_Workout_Plan").document(User.instance?.UserId.toString()).collection("Workoutplan")
                        .document(title).delete()
                    dialog.dismiss()

                }

                alertDialog.setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }

                alertDialog.show()
            }
        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        biding = CustomWorkoutplanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(biding)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        var data = itemlist[position]
        holder.bind(data)
        holder.itemView.setOnClickListener{
            OnClik(data)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}