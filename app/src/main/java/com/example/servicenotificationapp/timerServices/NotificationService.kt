package com.example.servicenotificationapp.timerServices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.servicenotificationapp.timerUtil.Actions
import com.example.servicenotificationapp.timerUtil.TIMER_CHANNEL_ID
import com.example.servicenotificationapp.timerUtil.TIMER_CHANNEL_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationService : Service() {
    private lateinit var notificationManager: NotificationManager
    private val timerNotificationModule = TimerNotificationModule()
    override fun onBind(intent: Intent?) = null
    override fun onCreate() {
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.RESUME.toString() -> {}
            Actions.PAUSE.toString() -> {}
            Actions.STOP.toString() -> {}
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        notificationManager = timerNotificationModule.provideNotificationManager(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TIMER_CHANNEL_ID,
                TIMER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(contentText: String) : NotificationCompat.Builder {
        val notificationBuilder = timerNotificationModule.provideNotificationBuilder(this)
        notificationBuilder.setContentText(contentText)
        return notificationBuilder
    }
}

data class NotificationData(val id: Int, val notificationBuilder: NotificationCompat.Builder)