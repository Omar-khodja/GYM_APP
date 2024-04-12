package com.example.gym_app.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym_app.Activity.Workout_plan.DisplayExerciseList_Activity
import com.example.gym_app.Activity.Messages.MessagesActivity
import com.example.gym_app.Activity.Search_Activity
import com.example.gym_app.Adapter.CardView_CreateAdapter
import com.example.gym_app.CardView_CreateData
import com.example.gym_app.R
import com.example.gym_app.databinding.FragmentHomeBinding


@Suppress("UNREACHABLE_CODE")
class HomeFegment : Fragment() {
    lateinit var biding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        biding = FragmentHomeBinding.inflate(inflater, container, false)
        val itemlis: MutableList<CardView_CreateData> = mutableListOf()
        itemlis.add(CardView_CreateData(R.drawable.abs, "Abs", "Abs Exersice", "9 Videos"))
        itemlis.add(CardView_CreateData(R.drawable.back, "Back", "Back Exersice", "4 Videos"))

        biding.createRecyclerView.adapter = CardView_CreateAdapter(itemlis) { item ->
            val intent = Intent(context, DisplayExerciseList_Activity::class.java)
            intent.putExtra("path", item.title)
            startActivity(intent)
        }
        biding.appbar.title = "Home"
        biding.appbar.apply {
            inflateMenu(R.menu.messages_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.message -> {
                       startActivity(Intent(context,MessagesActivity::class.java))
                        true
                    }
                    R.id.Search -> {
                        startActivity(Intent(context,Search_Activity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }
        setHasOptionsMenu(true)
        return biding.root
    }




    override fun onStart() {
        super.onStart()
    }


}