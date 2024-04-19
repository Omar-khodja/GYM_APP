package com.example.gym_app.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.example.gym_app.Activity.Client_Profile_Activity
import com.example.gym_app.Activity.Coach_Profile_Activity
import com.example.gym_app.Adapter.Coach_Search_Adapter
import com.example.gym_app.NewMessageData
import com.example.gym_app.databinding.FragmentChoachSearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CoachSearch_Fragment : Fragment() {
    lateinit var biding:FragmentChoachSearchBinding
    var db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        biding = FragmentChoachSearchBinding.inflate(inflater,container,false)
        val collection = db.collection("Users")

        fetchCoach(collection)
        biding.editText2.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
            }
            true
        }



        return biding.root
    }
    private fun search() {
        var text = biding.editText2.text.toString().trim()
        if (text.isNotEmpty()) {
            var collection = db.collection("Users").whereEqualTo("username", text)
            fetchCoach(collection)
        }
    }

    private fun fetchCoach(collection: Query) {
        collection.get().addOnSuccessListener {
            var itemlist = mutableListOf<NewMessageData>()
            for(doc in it.documents) {
                var type = doc.getString("type").toString()
                if (type == "Coach") {
                    var username = doc.getString("username").toString()
                    var userid = doc.getString("userId").toString()
                    var imgUrl = doc.getString("ProfileimagUri").toString()
                    itemlist.add(NewMessageData(userid, username,type, imgUrl))
                }
                biding.RecyclerView.adapter = Coach_Search_Adapter(itemlist){
                    if(it.type == "Coach"){
                        val intent = Intent(context, Coach_Profile_Activity::class.java)
                        val id = it.userId
                        val img = it.imguri
                        val username = it.username
                        intent.putExtra("id",id)
                        intent.putExtra("img",img)
                        intent.putExtra("username",username)
                        startActivity(intent)

                    }else {
                        val intent = Intent(context, Client_Profile_Activity::class.java)
                        val id = it.userId
                        val img = it.imguri
                        val username = it.username
                        intent.putExtra("id",id)
                        intent.putExtra("img",img)
                        intent.putExtra("username",username)
                        startActivity(intent)

                    }
                }

            }


        }

    }

}