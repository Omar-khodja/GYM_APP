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
import com.example.gym_app.databinding.FragmentHomeBinding


class HomeFegment : Fragment() {
    lateinit var biding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        biding = FragmentHomeBinding.inflate(inflater, container, false)
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







}