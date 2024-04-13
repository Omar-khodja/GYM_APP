package com.example.gym_app.Adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.NewMessageData
import com.example.gym_app.R
import com.example.gym_app.Singlton.DisplayVideo_Singlton
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.CustomUsersBinding
import com.google.firebase.firestore.FirebaseFirestore

class Share_Adapter(val title:String,val itemlist:MutableList<NewMessageData>,val OnclikItem:(NewMessageData)->Unit)
    :RecyclerView.Adapter<Share_Adapter.ViewHolder>(){
    lateinit var biding: CustomUsersBinding

    class ViewHolder(var biding : CustomUsersBinding,val Title:String):RecyclerView.ViewHolder(biding.root) {
        fun bind(data : NewMessageData){
            Glide.with(biding.root)
                .load(Uri.parse(data.imguri))
                .into(biding.imageView1)
            biding.textView.text = data.username
            biding.btn.visibility = View.VISIBLE
            biding.btn.setOnClickListener{
                AddWorkoutPlan(data.userId,Title)
                biding.btn.text = "Sent"
                biding.btn.background = ContextCompat.getDrawable(biding.root.context, R.drawable.unablebtn)
                biding.btn.isEnabled = false

            }

        }

        private fun AddWorkoutPlan(userId: String, Title: String) {
            var db = FirebaseFirestore.getInstance()
            var collection = db.collection("Client_Workout_Plan")
                .document(userId)
                .collection("workoutplan")
                .document(Title)
                .collection(Title)
            var collection2 = db.collection("Coach_Workout_Plan")
                .document(User.instance?.UserId.toString())
                .collection("Workoutplan")
                .document(Title)
                .collection(Title)
            collection2.get().addOnSuccessListener {
                for(doc in it.documents){
                    var id = doc.id
                    var ListName = doc.getString("ListName").toString()
                    var CoachName = doc.getString("CoachName").toString()
                    var CoachId = doc.getString("CoachId").toString()
                    var VideoUrl = doc.getString("VideoUrl").toString()
                    var ImahUrl = doc.getString("ImahUrl").toString()
                    var des = doc.getString("des").toString()
                    var VideoName = doc.getString("VideoName").toString()
                    var Note = doc.getString("Note").toString()
                    var data = hashMapOf(
                        "ListName" to ListName ,
                        "CoachName" to CoachName,
                        "CoachId" to CoachId,
                        "VideoUrl" to VideoUrl,
                        "ImahUrl" to ImahUrl,
                        "des" to des,
                        "VideoName" to VideoName,
                        "Note" to Note
                    )
                  collection.document(id).set(data)
                }

            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Share_Adapter.ViewHolder {
        biding = CustomUsersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(biding, title)
    }

    override fun onBindViewHolder(holder: Share_Adapter.ViewHolder, position: Int) {
        val data = itemlist[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            OnclikItem(data) // Call the lambda when an item is clicked
        }    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}