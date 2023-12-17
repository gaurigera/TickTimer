package com.example.servicenotificationapp.timerServices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.servicenotificationapp.timerServices.shared.TimerRepository
import com.example.servicenotificationapp.timerUtil.Actions
import com.example.servicenotificationapp.timerUtil.NOTIFICATION_ID
import com.example.servicenotificationapp.timerUtil.RESUME_VALUE
import com.example.servicenotificationapp.timerUtil.TIMER_CHANNEL_ID
import com.example.servicenotificationapp.timerUtil.TIMER_CHANNEL_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationService : Service() {
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    companion object {
        private val timerRepository = TimerRepository()

        fun updateTime(value: Int) {
//            timerRepository.setTimerSeconds(value) TODO
        }

        fun timerLiveData() = timerRepository
    }

    private var isRunning: Boolean = false

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val timerNotificationModule = TimerNotificationModule()
        notificationBuilder = timerNotificationModule.provideNotificationBuilder(this)
        notificationManager = timerNotificationModule.provideNotificationManager(this)
        when (intent?.action) {
            Actions.RESUME.toString() -> {
                intent.getIntExtra(RESUME_VALUE, 6000)
                    .also {
                        updateTime(it)
                        println(it)
                    }
                isRunning = true
                startForegroundService()
                setTimerControlButtons(!isRunning)
                timerCountDown {
                    updateNotification(it)
                }
            }

            Actions.PAUSE.toString() -> {
                isRunning = false
                setTimerControlButtons(!isRunning)
            }

            Actions.STOP.toString() -> {
                isRunning = false
                stopForegroundService()
            }
        }
        return START_NOT_STICKY
    }

    private fun timerCountDown(onTick: (string: String) -> Unit) {
//TODO
        //        CoroutineScope(Dispatchers.Main).launch {
//            while (isRunning && timerLiveData().value != 0) {
//                val temp = timerLiveData().value
//                if (temp != null) {
//                    updateTime(temp - 1)
//                }
//                delay(1000)
//                onTick(secondsToTimeString(timerLiveData().value!!))
//            }
//        }
    }

    private fun setTimerControlButtons(isPaused: Boolean) {
        notificationBuilder.clearActions()
        if (!isPaused)
            notificationBuilder
                .addAction(0, "PAUSE", TimerActionModule.providePausePendingIntent(this))
        else
            notificationBuilder
                .addAction(0, "RESUME", TimerActionModule.provideResumePendingIntent(this))

        notificationBuilder
            .addAction(0, "STOP", TimerActionModule.provideStopPendingIntent(this))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TIMER_CHANNEL_ID,
                TIMER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateNotification(contentText: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(contentText)
                .build()
        )
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}