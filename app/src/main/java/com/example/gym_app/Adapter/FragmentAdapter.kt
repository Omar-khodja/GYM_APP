package com.example.gym_app.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragmentManager: FragmentManager,lc:Lifecycle):FragmentStateAdapter(fragmentManager,lc) {
    var fragmentlist : ArrayList<Fragment> = ArrayList()
    fun AddFragmentToList(fragment: Fragment){
         fragmentlist.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentlist.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentlist.get(position)
    }

}