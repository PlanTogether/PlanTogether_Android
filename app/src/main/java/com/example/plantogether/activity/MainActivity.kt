package com.example.plantogether.activity

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.plantogether.adapter.MyViewPagerAdapter
import com.example.plantogether.fragment.CalendarFragment
import com.example.plantogether.R
import com.example.plantogether.databinding.ActivityMainBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding:ActivityMainBinding
    lateinit var bnv: BottomNavigationView

    lateinit var db : EventDatabase

    lateinit var rdb: DatabaseReference
    var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bnv = binding.bottomNav
        setContentView(binding.root)

        db = EventDatabase.getDatabase(this)

        userName = intent.getStringExtra("userName").toString()
        linkFirebase()
        initLayout()
        //getHashKey()
        initFragment()
    }

    private fun initFragment() {
        val fragment = supportFragmentManager.beginTransaction()
        val calendarFragment = CalendarFragment()
        fragment.commit()
    }
    private fun initLayout() {
        binding.viewpager.adapter = MyViewPagerAdapter(this, userName)

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

    private fun linkFirebase() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                if (deepLink != null) {
                    var title = deepLink.getQueryParameter("title")
                    var place = deepLink.getQueryParameter("place")
                    var date = deepLink.getQueryParameter("date")
                    var detail = deepLink.getQueryParameter("detail")
                    val event = Event(0,1, title!!, place!!, date!!, "", detail!!)
                    Log.d("query?",title)
                    Log.d("query?",date)
                    Log.d("query?",detail)
                    Log.d("query?",place)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.eventDao().insertEvent(event)
                    }
                }


                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...
            }
            .addOnFailureListener(this) { e -> Log.w("firbase", "getDynamicLink:onFailure", e) }
    }


//    private fun getHashKey() {
//        try {
//            val information =
//                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
//            val signatures = information.signingInfo.apkContentsSigners
//            val md = MessageDigest.getInstance("SHA")
//            for (signature in signatures) {
//                val md: MessageDigest
//                md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                var hashcode = String(Base64.encode(md.digest(), 0))
//                Log.d("hashcode", "" + hashcode)
//            }
//        } catch (e: Exception) {
//            Log.d("hashcode", "에러::" + e.toString())
//
//        }
//    }
}