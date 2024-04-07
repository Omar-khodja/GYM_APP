package com.example.gym_app.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.gym_app.R
import com.example.gym_app.databinding.ActivityHomeBinding
import com.example.gym_app.fragment.CreateFragment
import com.example.gym_app.Adapter.FragmentAdapter
import com.example.gym_app.User
import com.example.gym_app.fragment.HomeFragment
import com.example.gym_app.fragment.ProfileFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class HomeActivity : AppCompatActivity() {
    lateinit var adapter : FragmentAdapter
    lateinit var biding:ActivityHomeBinding
    var auth= Firebase.auth
    var db= FirebaseFirestore.getInstance()
    var userId = auth.currentUser?.uid
    lateinit var tabarry:Array<Int>
    var userCollection = db.collection("Users").whereEqualTo("userId", userId)
    var type =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        biding = DataBindingUtil.setContentView(this , R.layout.activity_home)
        adapter = FragmentAdapter(supportFragmentManager , lifecycle)
        userCollection.whereEqualTo("userId",userId).get()
            .addOnSuccessListener { qury ->
                if (!qury.isEmpty) {
                    val document = qury.documents[0] // Assuming there's only one document with the matching user ID

                    User.UserName = document.getString("username").toString()
                    User.UserId = userId.toString()
                    User.ProfileimagUri = Uri.parse(document.getString("ProfileimagUri").toString())
                    User.Email = document.getString("email").toString()
                    User.UserPhonenb = document.getString("userphonenb").toString()
                    User.CoachOrClient = document.getString("type").toString()
                    type = document.getString("type").toString()
                    if(type == "Coach") {
                        tabarry = arrayOf(R.drawable.homee, R.drawable.add, R.drawable.profile)
                        adapter.AddFragmentToList(HomeFragment())
                        adapter.AddFragmentToList(CreateFragment())
                        adapter.AddFragmentToList(ProfileFragment())
                    }else{
                        tabarry = arrayOf(R.drawable.homee, R.drawable.profile)
                        adapter.AddFragmentToList(HomeFragment())
                        adapter.AddFragmentToList(ProfileFragment())

                    }
                    biding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    biding.viewpager.adapter = adapter
                    TabLayoutMediator(
                        biding.tablayout,
                        biding.viewpager
                    ){
                            tab,position ->
                        tab.setIcon(tabarry[position])
                    }.attach()
                }
            }

        Log.i("tagy",type)




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.messages_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle menu item clicks
        when (item.itemId) {
            R.id.message -> {
                startActivity(Intent(this,MessagesActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)

    }


}