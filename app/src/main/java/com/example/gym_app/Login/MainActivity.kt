package com.example.gym_app.Login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Activity.HomeActivity
import com.example.gym_app.R
import com.example.gym_app.Singlton.User
import com.example.gym_app.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var auth : FirebaseAuth
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        auth = Firebase.auth
        binding.loginbtn.setOnClickListener{
            var intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
        }
        binding.singupbtn.setOnClickListener{
            var intent = Intent(this , SingupActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onStart() {
        if(auth.currentUser != null){
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        super.onStart()
    }


    }
