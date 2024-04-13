package com.example.gym_app.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gym_app.ChatActivityData
import com.example.gym_app.NewMessageData
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.NotificationRequstBinding
import com.google.firebase.firestore.FirebaseFirestore

class Notification_Adapter(val itemlist : MutableList<NewMessageData>)
    :RecyclerView.Adapter<Notification_Adapter.ViewHolder>() {
    lateinit var biding:NotificationRequstBinding


    class ViewHolder(var biding :NotificationRequstBinding ): RecyclerView.ViewHolder(biding.root) {
        fun bind(data : NewMessageData){
            biding.data = data
            Glide.with(biding.root)
                .load(data.imguri)
                .into(biding.PrifileImg)

            buttonenable(data.userId)
            biding.Confirm.setOnClickListener{
                addtofollower(data.imguri,data.userId,data.username)
            }
            biding.delete.setOnClickListener{
                delete(data.userId)
            }

        }

        private fun delete(userId: String) {
            var db = FirebaseFirestore.getInstance()
            var collection = db.collection("Coach_Follow_request")
                .document(User.instance?.UserId.toString())
                .collection("Followers")
                .document(userId).delete()

        }

        private fun buttonenable(userId: String) {
            var db = FirebaseFirestore.getInstance()
            var collection = db.collection("Coach_Followers")
                .document(User.instance?.UserId.toString())
                .collection("Followers")
            collection.addSnapshotListener { value, error ->
                if (error != null) {
                    Log.i("tagy", "Error listening for messages: $error")
                    return@addSnapshotListener
                }

                value?.documents?.forEach { doc ->

                    val id = doc.id

                    // Check if the message is new
                    if (id == userId) {
                        biding.Confirm.background = ContextCompat.getDrawable(biding.root.context, R.drawable.unablebtn)
                        biding.Confirm.text = "Confirmed"
                        biding.Confirm.isEnabled = false
                        biding.delete.visibility = View.INVISIBLE
                        biding.mesg.visibility = View.INVISIBLE
                        biding.Confirm.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }
        }

        private fun addtofollower(imguri: String, userId: String, username: String) {
            var db = FirebaseFirestore.getInstance()
            var collection = db.collection("Coach_Followers")
                .document(User.instance?.UserId.toString())
                .collection("Followers").document(userId)
            var data = hashMapOf(
                "UserId" to userId,
                "UserName" to username,
                "imagUri" to imguri
            )
            collection.set(data)
        }

    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemlist[position]
        holder.bind(data)    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        biding = NotificationRequstBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(biding)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }


}
