package com.example.gym_app.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.NewMessageData
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.CustomUsersBinding
import com.google.firebase.firestore.FirebaseFirestore

class Share_Adapter(val title:String,val itemlist:MutableList<NewMessageData>)
    :RecyclerView.Adapter<Share_Adapter.ViewHolder>() {
    lateinit var biding: CustomUsersBinding

    class ViewHolder(var biding: CustomUsersBinding, val Title: String) :
        RecyclerView.ViewHolder(biding.root) {
        fun bind(data: NewMessageData) {
            Glide.with(biding.root)
                .load(Uri.parse(data.imguri))
                .into(biding.imageView1)
            biding.textView.text = data.username
            biding.btn.visibility = View.VISIBLE

            biding.btn.setOnClickListener {
                AddWorkoutPlan(data.userId, Title, data.username, data.imguri)
                biding.btn.text = "Sent"
                biding.btn.background =
                    ContextCompat.getDrawable(biding.root.context, R.drawable.unablebtn)
                biding.btn.isEnabled = false

            }

        }

        private fun AddWorkoutPlan(
            userId: String,
            Title: String,
            username: String,
            imguri: String
        ) {
            var db = FirebaseFirestore.getInstance()
            var collectionClient = db.collection("Client_Workout_Plan")
                .document(userId)
                .collection("Workoutplan")

            var collectionCoach = db.collection("Coach_Workout_Plan")
                .document(User.instance?.UserId.toString())
                .collection("Workoutplan")
                .document(Title)
            collectionCoach.get().addOnSuccessListener {
                if (it.exists()) {
                    val plan = it.getString("Title").toString()
                    val data = hashMapOf(
                        "Title" to plan,
                        "CoachId" to User.instance?.UserId.orEmpty()
                    )
                    collectionClient.document(plan).set(data)
                }
            }


            //represent goal of progress
            var i = 0
            collectionCoach.collection("List")
                .get().addOnSuccessListener {
                    for (doc in it.documents) {
                        i++
                        val name = doc.id
                        collectionCoach.collection("List")
                            .document(name)
                            .collection(name).get().addOnSuccessListener {
                                for (doc in it.documents) {
                                    val CoachId = doc.getString("CoachId")
                                    val CoachName = doc.getString("CoachName")
                                    val ImahUrl = doc.getString("ImahUrl")
                                    val ListName = doc.getString("ListName")
                                    val Note = doc.getString("Note")
                                    var sets = doc.getString("sets")
                                    var repsets = doc.getString("repsets")
                                    val VideoName = doc.getString("VideoName")
                                    val VideoUrl = doc.getString("VideoUrl")
                                    val Workoutplan = doc.getString("Workoutplan")

                                    val data = hashMapOf(
                                        "CoachId" to CoachId,
                                        "CoachName" to CoachName,
                                        "ImahUrl" to ImahUrl,
                                        "ListName" to ListName,
                                        "Note" to Note,
                                        "sets" to sets,
                                        "repsets" to repsets,
                                        "VideoName" to VideoName,
                                        "VideoUrl" to VideoUrl,
                                        "Workoutplan" to Workoutplan
                                    )
                                    val data2 = hashMapOf(
                                        "ListName" to ListName,
                                        "Workoutplan" to Workoutplan
                                    )
                                    collectionClient.document(Title).collection("List").document(name)
                                        .set(data2)
                                        .addOnSuccessListener {
                                            collectionClient.document(Title).collection("List")
                                                .document(name)
                                                .collection(name)
                                                .add(data)
                                        }

                                }
                            }
                    }
                    collectionCoach.get().addOnSuccessListener {
                        if (it.exists()) {
                            i = i*4
                            val plan = it.getString("Title").toString()
                            val data = hashMapOf(
                                "Title" to plan,
                                "CoachId" to User.instance?.UserId,
                                "ClientName" to username,
                                "ClientImag" to imguri,
                                "goal" to i.toString(),
                                "progress" to "0"
                            )
                            db.collection("Client_Progress")
                                .document(User.instance?.UserId.toString())
                                .collection("Client")
                                .document(userId).set(data)


                        }

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
        holder.bind(data)    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}