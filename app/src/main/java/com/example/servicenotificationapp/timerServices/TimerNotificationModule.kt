package com.example.servicenotificationapp.timerServices

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import com.example.servicenotificationapp.R
import com.example.servicenotificationapp.timerUtil.TIMER_CHANNEL_ID
import dagger.hilt.android.qualifiers.ApplicationContext

class TimerNotificationModule {
    fun provideNotificationBuilder(
//        @ApplicationContext
        context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, TIMER_CHANNEL_ID)
            .setContentTitle("Timer")
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setVisibility(VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(TimerActionModule.provideClickPendingIntent(context))
    }

    fun provideNotificationManager(
//        @ApplicationContext
        context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
    }
}