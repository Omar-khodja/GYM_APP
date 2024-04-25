package com.example.gym_app.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Activity.HomeActivity
import com.example.gym_app.R
import com.example.gym_app.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    var auth = Firebase.auth
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)



        binding.loginbtn.setOnClickListener {
            var email = binding.loginEmail.text.toString().trim()
            var password = binding.loginPassword.text.toString().trim()
            if(email.isNotEmpty() && password.isNotEmpty()) {

                binding.progressBar2.visibility = View.VISIBLE
                LoginWithPassword(
                    email,
                    password
                )
            }else{
                Toast.makeText(this, "please enter all information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun LoginWithPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    binding.progressBar2.visibility = View.INVISIBLE
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }


}