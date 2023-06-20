package com.example.plantogether.fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.R
import com.example.plantogether.activity.EventInfoActivity
import com.example.plantogether.activity.LoginActivity
import com.example.plantogether.activity.MainActivity
import com.example.plantogether.adapter.NoticeAdapter
import com.example.plantogether.data.NoticeData
import com.example.plantogether.databinding.FragmentNoticeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class NoticeFragment : Fragment() {
    //오늘 기준 이후의 이벤트를 가져와서
    //오늘의 일정, 변경된 이벤트, 초대장 수락 거절등의 정보를 가져온다.


    lateinit var binding : FragmentNoticeBinding
    lateinit var adapter : NoticeAdapter
    var data : ArrayList<NoticeData> = ArrayList()

    lateinit var noticedb: DatabaseReference
    var userName: String = ""
    var firstLoad = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = arguments?.getString("userName").toString()
        initRecyclerView()
        CoroutineScope(Dispatchers.IO).launch {
            initData()
        }
        // println("사용자명 : " + userName + " in EventFragment")
    }

    private fun initData() {
        noticedb = Firebase.database.getReference("$userName/Notices")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                var time = 0L
                var recentNotice :NoticeData? = null
                for (childSnapshot in snapshot.children) {
                    val event = childSnapshot.getValue(NoticeData::class.java)
                    event?.let {
                        if (it.time > time) {
                            time = it.time
                            recentNotice = it
                        }
                          data.add(it)
                    }
                }

                adapter.items = data
                adapter.notifyDataSetChanged()
                if (firstLoad) {
                    firstLoad = false
                } else {
                    if (recentNotice != null ) {
                        notification(recentNotice!!.title + " " + recentNotice!!.notice)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 처리 실패 시 호출되는 메서드
            }
        }

        noticedb.addValueEventListener(eventListener)
    }
    private fun initRecyclerView() {

        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        adapter = NoticeAdapter(data)
        adapter.itemClickListener = object : NoticeAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int, noticeData: NoticeData) {
                val intent = Intent(context, EventInfoActivity::class.java)
                intent.putExtra("userName", userName)
                Log.d("id",noticeData.eventKey)
                intent.putExtra("id", noticeData.eventKey)
                context?.startActivity(intent)
            }

        }
        binding.noticeRecyclerView.adapter = adapter

    }

    private fun notification(text: String) {
        Log.d("notification", text)
        val channelId = "my_channel_id"
        val channelName = "My Channel"
        val channelDescription = "알림보내기."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
// 2. 알림 빌더 설정
        val notificationBuilder = NotificationCompat.Builder(requireContext(), channelId)
            .setContentTitle("플랜투게더")
            .setWhen(System.currentTimeMillis())
            .setContentText(text)
            .setSmallIcon(R.drawable.notification_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)

// 3. 알림 표시
        val notificationId = 0 // 알림 식별자 (고유한 값)
        val notification = notificationBuilder?.build()
        notificationManager.notify(notificationId, notificationBuilder.build())




    }
}