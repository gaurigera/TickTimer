package com.example.servicenotificationapp.timerServices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat.Builder
import com.example.servicenotificationapp.R
import com.example.servicenotificationapp.data.TimerRowState
import com.example.servicenotificationapp.timerServices.shared.TimerRepository
import com.example.servicenotificationapp.timerUtil.Actions
import com.example.servicenotificationapp.timerUtil.INDEX_VALUE
import com.example.servicenotificationapp.timerUtil.TIMER_CHANNEL_ID
import com.example.servicenotificationapp.timerUtil.TIMER_CHANNEL_NAME
import com.example.servicenotificationapp.timerUtil.decrementTimer
import com.example.servicenotificationapp.timerUtil.timerStateToFormat
import com.example.servicenotificationapp.timerUtil.timerStateToSeconds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: Builder

    @Inject
    lateinit var timerRepository: TimerRepository

    private val isPaused = mutableMapOf<Int, Boolean>()
    private var timerJobs = mutableMapOf<Int, Job>()
    private var timerPlayer = mutableMapOf<Int, MediaPlayer>()
    private val serviceScope = CoroutineScope(Dispatchers.Default)
    override fun onBind(intent: Intent?) = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var index: Int = 0
        createNotificationChannel()
        when (intent?.action) {
            Actions.RESUME.toString() -> {
                index = intent.getIntExtra(INDEX_VALUE, 0)
                isPaused[index] = false
                setActionButtons(index, false)
                startForegroundService(index)
                timerRepository.value?.get(index)?.let { timerStateToFormat(it.timerState) }?.let {
                    createNotification(
                        index,
                        it
                    )
                }
                timerJobs[index] = serviceScope.launch {
                    while (!isPaused[index]!!) {
                        var temp = timerRepository.value?.get(index)
                        if (temp != null) {
                            if (timerStateToSeconds(temp.timerState) == 0) {
                                playSound(index)
                                break
                            }
                            temp = TimerRowState(decrementTimer(temp.timerState), false)
                            timerRepository.updateTimerRepository(
                                index, temp.timerState
                            )
                            updateNotification(index, timerStateToFormat(temp.timerState))
                        }
                        delay(1000)
                    }
                }
            }

            Actions.PAUSE.toString() -> {
                index = intent.getIntExtra(INDEX_VALUE, 0)
                isPaused[index] = true
                setActionButtons(index, true)
            }

            Actions.STOP.toString() -> {
                index = intent.getIntExtra(INDEX_VALUE, 0)
                isPaused[index] = true
                if (timerPlayer.containsKey(index))
                    stopSound(index)
                timerJobs[index]?.cancel()
                timerJobs.remove(index)
                isPaused.remove(index)
                timerRepository.removeAtIndex(index)
                stopForegroundService(index)
            }
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TIMER_CHANNEL_ID,
                TIMER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(index: Int, contentText: String) {
        notificationBuilder
            .setContentText(contentText)
            .setContentIntent(TimerActionModule.provideClickPendingIntent(this))

        notificationManager.notify(index, notificationBuilder.build())
    }

    private fun updateNotification(index: Int, contentText: String) {
        notificationBuilder.setContentText(contentText)
        notificationManager.notify(index, notificationBuilder.build())
    }

    private fun setActionButtons(index: Int, isPaused: Boolean) {
        notificationBuilder.clearActions()
        if (isPaused) {
            notificationBuilder
                .addAction(0, "RESUME", TimerActionModule.provideResumePendingIntent(this))
        } else {
            notificationBuilder
                .addAction(0, "PAUSE", TimerActionModule.providePausePendingIntent(this))
        }
        notificationBuilder
            .addAction(0, "STOP", TimerActionModule.provideStopPendingIntent(this))
        notificationManager.notify(index, notificationBuilder.build())
    }

    private fun startForegroundService(index: Int) {
        startForeground(index, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun stopForegroundService(index: Int) {
        notificationManager.cancel(index)
        stopForeground(STOP_FOREGROUND_REMOVE)

        if (isPaused.isEmpty()) {
            stopSelf()
        }
    }

    private fun playSound(index: Int) {
        val mediaPlayer = MediaPlayer.create(this@NotificationService, R.raw.windchimesound)
        mediaPlayer.start()
        timerPlayer[index] = mediaPlayer
    }

    private fun stopSound(index: Int) {
        timerPlayer[index]?.stop()
    }
}