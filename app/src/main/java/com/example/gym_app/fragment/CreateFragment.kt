package com.example.gym_app.fragment

import android.content.Intent
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym_app.Activity.DisplayExerciseList_Activity
import com.example.gym_app.Adapter.CardView_CreateAdapter
import com.example.gym_app.CardView_CreateData
import com.example.gym_app.R
import com.example.gym_app.databinding.FragmentCreateBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CreateFragment : Fragment() {
    lateinit var biding:FragmentCreateBinding
    lateinit var db:FirebaseFirestore
    lateinit var storage:FirebaseStorage
    lateinit var storageReference: StorageReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        biding = FragmentCreateBinding.inflate(inflater,container,false)
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        val itemlis:MutableList<CardView_CreateData> = mutableListOf()
        itemlis.add(CardView_CreateData(R.drawable.abs,"Abs","Abs Exersice","9 Videos"))
        itemlis.add(CardView_CreateData(R.drawable.back,"Back","Back Exersice","4 Videos"))

        biding.createRecyclerView.adapter = CardView_CreateAdapter(itemlis){item->
            val intent = Intent(context,DisplayExerciseList_Activity::class.java)
            intent.putExtra("path",item.title)
            startActivity(intent)
        }

        return biding.root
    }

}