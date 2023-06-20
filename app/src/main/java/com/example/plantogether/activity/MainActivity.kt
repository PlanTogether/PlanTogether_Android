package com.example.plantogether.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.plantogether.adapter.MyViewPagerAdapter
import com.example.plantogether.fragment.CalendarFragment
import com.example.plantogether.R
import com.example.plantogether.data.EventData
import com.example.plantogether.data.NoticeData
import com.example.plantogether.databinding.ActivityMainBinding
import com.example.plantogether.roomDB.EventDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding:ActivityMainBinding
    lateinit var bnv: BottomNavigationView

    lateinit var db : EventDatabase

    lateinit var rdb: DatabaseReference
    lateinit var noticedb: DatabaseReference

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
                Log.d("dynamic", "are you here")
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                Log.d("dynamic", deepLink.toString())
                if (deepLink != null) {
                    // 이벤트를 생성한, 즉 초대자의 경로에 들어가서 participantName을 업데이트.
                    var id = deepLink.getQueryParameter("id").toString()
                    var inviter = deepLink.getQueryParameter("inviter").toString()
                    rdb = Firebase.database.getReference("$inviter/Events")
                    noticedb = Firebase.database.getReference("$inviter/Notices")
                    rdb.child(id).child("participantName")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val typeIndicator =
                                object : GenericTypeIndicator<MutableList<String>>() {}
                            val participantNames =
                                snapshot.getValue(typeIndicator)
                            CoroutineScope(Dispatchers.IO).launch {

                                var flag = true
                                for (invitee in participantNames!!) {
                                    if (invitee == userName) {
                                       flag = false
                                    }
                                }
                                if (flag) {
                                    participantNames?.add(userName)
                                    rdb.child(id).child("participantName")
                                        .setValue(participantNames)
                                }

                                // 업데이트한 데이터를 본인의 경로에 복사
                                val sourceReference =
                                    Firebase.database.getReference("$inviter/Events")
                                val destinationReference =
                                    Firebase.database.getReference("$userName/Events")

                                val newNoticeRef = noticedb.push()
                                val newNoticeRefKey = newNoticeRef.key
                                val now = System.currentTimeMillis()
                                sourceReference.child(id).
                                addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val eventData = snapshot.getValue(EventData::class.java)
                                        destinationReference.child(id).setValue(eventData)
                                        if (flag) {
                                            val text = "${userName}님이 초대되었습니다."
                                            val noticeData = NoticeData(
                                                newNoticeRefKey.toString(),
                                                eventData?.title.toString(), now, text
                                            )
                                            newNoticeRef.setValue(noticeData)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Handle onCancelled event
                                    }
                                })

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle onCancelled event
                        }
                    })



                    Log.d("query?",id)
                    Log.d("query?",inviter)
                }
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