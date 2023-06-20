package com.example.plantogether.reciever

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.plantogether.R
import com.example.plantogether.activity.MainActivity
import kotlinx.coroutines.withContext

class EventBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Set the alarm here.
            showNotification(context, intent)
        }

    }

    private fun showNotification(context: Context, intent: Intent) {
        val channelId = "my_channel_id"


        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setContentTitle("일정 알림")
            .setContentText("오늘 진행되는 이벤트가 있습니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

// 알림을 표시하기 위해 NotificationManager를 사용합니다.

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pintent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "일정 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

// 알림을 표시합니다.
        val notificationId = 100
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}