package com.example.plantogether.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.plantogether.fragment.CalendarFragment
import com.example.plantogether.fragment.EventFragment
import com.example.plantogether.fragment.NoticeFragment

class MyViewPagerAdapter(fragmentActivity: FragmentActivity, private val un: String):
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> { CalendarFragment().apply {
                    arguments = Bundle().apply {
                        putString("userName", un)
                    }
                }
            }

            1 -> EventFragment().apply {
                arguments = Bundle().apply {
                    putString("userName", un)
                }
            }
            2 -> NoticeFragment().apply {
                arguments = Bundle().apply {
                    putString("userName", un)
                }
            }
            else -> CalendarFragment()
        }
    }
}