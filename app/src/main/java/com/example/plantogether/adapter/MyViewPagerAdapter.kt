package com.example.plantogether.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.plantogether.fragment.CalendarFragment
import com.example.plantogether.fragment.EventFragment
import com.example.plantogether.fragment.NoticeFragment

class MyViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0->return CalendarFragment()
            1->return EventFragment()
            2->return NoticeFragment()
            else->return CalendarFragment()
        }
    }
}