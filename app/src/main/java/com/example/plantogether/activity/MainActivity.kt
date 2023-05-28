package com.example.plantogether.activity

import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.example.plantogether.fragment.CalendarFragment
import com.example.plantogether.R
import com.example.plantogether.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val textArr =  arrayListOf<String>("캘린더", "이벤트","알림")
    val imgArr = arrayListOf<Int>(R.drawable.baseline_calendar_today_30_white,
        R.drawable.baseline_star_outline_30_white, R.drawable.baseline_notifications_none_30_white)
    val imgSelectedArr = arrayListOf<Int>(R.drawable.baseline_calendar_today_30_black,
        R.drawable.baseline_star_outline_30_black, R.drawable.baseline_notifications_none_30_black)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        binding.tabLayout.setBackgroundColor(Color.parseColor("#ACDBF6"));
        TabLayoutMediator(binding.tabLayout, binding.viewpager) {
                tab,pos ->
            // tab.text = textArr[pos]
            tab.setIcon(imgArr[pos])
        }.attach()
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