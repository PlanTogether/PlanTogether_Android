package com.example.plantogether.activity

import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.plantogether.fragment.CalendarFragment
import com.example.plantogether.R
import com.example.plantogether.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import java.security.MessageDigest

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding:ActivityMainBinding
    lateinit var bnv: BottomNavigationView
    val textArr =  arrayListOf<String>("캘린더", "이벤트","알림")
    val imgArr = arrayListOf<Int>(R.drawable.icon_calendar_white,
        R.drawable.icon_eventstar_white, R.drawable.icon_notification_white)
    val imgSelectedArr = arrayListOf<Int>(R.drawable.icon_calendar_black,
        R.drawable.icon_eventstar_black, R.drawable.icon_notification_black)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bnv = binding.bottomNav
        setContentView(binding.root)
        initLayout()
        getHashKey()
        initFragment()
    }

    private fun initFragment() {
        val fragment = supportFragmentManager.beginTransaction()
        val calendarFragment = CalendarFragment()
        // fragment.replace(R.id.frameLayout, calendarFragment )
        fragment.commit()
    }
    private fun initLayout() {
        binding.viewpager.adapter = MyViewPagerAdapter(this)

        binding.viewpager.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNav.menu.getItem(position).isChecked = true
                }
            }
        )

        binding.bottomNav.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuCalendar -> {
                // ViewPager의 현재 item에 첫 번째 화면을 대입
                binding.viewpager.currentItem = 0
                return true
            }
            R.id.menuEvent -> {
                // ViewPager의 현재 item에 두 번째 화면을 대입
                binding.viewpager.currentItem = 1
                return true
            }
            R.id.menuNotification -> {
                // ViewPager의 현재 item에 세 번째 화면을 대입
                binding.viewpager.currentItem = 2
                return true
            }
            else -> {
                return false
            }
        }
    }


    private fun getHashKey() {
        try {
            val information =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = information.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                var hashcode = String(Base64.encode(md.digest(), 0))
                Log.d("hashcode", "" + hashcode)
            }
        } catch (e: Exception) {
            Log.d("hashcode", "에러::" + e.toString())

        }
    }
}