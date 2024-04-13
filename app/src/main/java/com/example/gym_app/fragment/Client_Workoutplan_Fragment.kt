package com.example.gym_app.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym_app.Activity.Messages.MessagesActivity
import com.example.gym_app.Activity.Search_Activity
import com.example.gym_app.R
import com.example.gym_app.databinding.FragmentClientWorkoutplanBinding

class Client_Workoutplan_Fragment : Fragment() {
    lateinit var biding :FragmentClientWorkoutplanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        biding = FragmentClientWorkoutplanBinding.inflate(inflater,container,false)
        biding.toolbar.title = "Home"
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
        return biding.root
    }

}