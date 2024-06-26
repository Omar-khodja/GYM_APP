package com.example.gym_app.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.gym_app.R
import com.example.gym_app.databinding.ActivityHomeBinding
import com.example.gym_app.fragment.HomeFegment
import com.example.gym_app.Adapter.FragmentAdapter
import com.example.gym_app.Login.MainActivity
import com.example.gym_app.Singlton.User
import com.example.gym_app.fragment.Client_Progresse_Fragment
import com.example.gym_app.fragment.Client_Workoutplan_Fragment
import com.example.gym_app.fragment.CoachSearch_Fragment
import com.example.gym_app.fragment.CreateWorkoutPlane_Fragment
import com.example.gym_app.fragment.ProfileFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    lateinit var biding: ActivityHomeBinding
    lateinit var adapter: FragmentAdapter
    var auth = Firebase.auth
    var db = FirebaseFirestore.getInstance()
    var userId = auth.currentUser?.uid
    lateinit var tabarry: Array<Int>
    var userCollection = db.collection("Users").whereEqualTo("userId", userId)
    var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        adapter = FragmentAdapter(supportFragmentManager, lifecycle)
        userCollection.whereEqualTo("userId", userId).get()
            .addOnSuccessListener { qury ->
                if (!qury.isEmpty) {
                    val document =
                        qury.documents[0] // Assuming there's only one document with the matching user ID
                    User.instance?.UserName = document.getString("username").toString()
                    User.instance?.UserId = userId.toString()
                    User.instance?.ProfileimagUri =
                        Uri.parse(document.getString("ProfileimagUri").toString())
                    User.instance?.Email = document.getString("email").toString()
                    User.instance?.UserPhonenb = document.getString("userphonenb").toString()
                    User.instance?.CoachOrClient = document.getString("type").toString()
                    type = document.getString("type").toString()
                    if (type == "Coach") {
                        tabarry = arrayOf(R.drawable.home,R.drawable.progress, R.drawable.profile)
                        adapter.AddFragmentToList(CreateWorkoutPlane_Fragment())
                        adapter.AddFragmentToList(Client_Progresse_Fragment())
                        adapter.AddFragmentToList(ProfileFragment())
                        User.instance?.VideoUri = Uri.parse(document.getString("VideoUri"))
                        User.instance?.Bio = document.getString("Bio").toString()

                    } else {
                        tabarry = arrayOf(R.drawable.home, R.drawable.coach, R.drawable.profile)
                        adapter.AddFragmentToList(Client_Workoutplan_Fragment())
                        adapter.AddFragmentToList(CoachSearch_Fragment())
                        adapter.AddFragmentToList(ProfileFragment())

                    }
                    biding.ViewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    biding.ViewPager2.adapter = adapter
                    TabLayoutMediator(
                        biding.TableLayout,
                        biding.ViewPager2
                    ) { tab, position ->
                        tab.setIcon(tabarry[position])

                    }.attach()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser ==null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}

