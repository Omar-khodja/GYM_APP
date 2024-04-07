package com.example.gym_app.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.gym_app.Activity.MessagesActivity
import com.example.gym_app.Activity.NewMessageActivity
import com.example.gym_app.Login.MainActivity
import com.example.gym_app.R
import com.example.gym_app.databinding.FragmentMessagesragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class HomeFragment : Fragment() {
    lateinit var binding: FragmentMessagesragmentBinding
    var auth= Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesragmentBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }
    override fun onStart() {
        if(auth.currentUser ==null){
            startActivity(Intent(context, MainActivity::class.java))
        }
        super.onStart()
    }

}


