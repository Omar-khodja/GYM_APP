package com.example.gym_app.Login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.gym_app.Activity.HomeActivity
import com.example.gym_app.R
import com.example.gym_app.databinding.ActivitySingupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class SingupActivity : AppCompatActivity() {
    lateinit var biding: ActivitySingupBinding
     var  auth = Firebase.auth
    val db = Firebase.firestore
     var storageReference = FirebaseStorage.getInstance().getReference()
     var imagUri :String =""


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_singup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this, R.layout.activity_singup)




        biding.singupbtn.setOnClickListener {
            var username = biding.singupUsername.text.toString().trim()
            var email = biding.singupEmail.text.toString().trim()
            var password = biding.singupPassword.text.toString().trim()
            var phone = biding.singupPhonenb.text.toString().trim()
           if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty()) {
                biding.progressBar.visibility = View.VISIBLE
                   creataccount()
            }else{
                Toast.makeText(this,"Please enter all your information",Toast.LENGTH_SHORT).show()
           }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun addUserTodb(userId: String?, email: String?, password: String) {
            val colaction = db.collection("Users").document(auth.currentUser?.uid.toString())
            if (biding.coach.isChecked) {

                val userMap = hashMapOf(
                    "userId" to userId,
                    "email" to email,
                    "username" to biding.singupUsername.text.toString().trim(),
                    "userphonenb" to biding.singupPhonenb.text.toString().trim(),
                    "type" to "Coach",
                    "ProfileimagUri" to imagUri,
                    "VideoUri" to "",
                    "Bio" to "",
                    "password" to password
                )
                colaction.set(userMap)
                    .addOnSuccessListener {
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Error adding user: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else if (biding.cliet.isChecked) {
                val userMap = hashMapOf(
                    "userId" to userId,
                    "email" to email,
                    "username" to biding.singupUsername.text.toString().trim(),
                    "userphonenb" to biding.singupPhonenb.text.toString().trim(),
                    "type" to "Client",
                    "ProfileimagUri" to imagUri,
                    "password" to password
                )
                colaction.set(userMap)
                    .addOnSuccessListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Error adding user: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    this,
                    "Please select user type",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }



    private fun creataccount() {
        val password = biding.singupPassword.text.toString()
        val email = biding.singupEmail.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    biding.progressBar.visibility = View.INVISIBLE
                    val user = auth.currentUser // Get the current user
                    user?.let {
                        val userId = it.uid
                        val userEmail = it.email
                        addUserTodb(userId, userEmail,password)
                    }
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAGY", "createUserWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAGY", "createUserWithEmail:failure", task.exception)
                        biding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

}



