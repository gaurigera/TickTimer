package com.example.servicenotificationapp.timerServices

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.servicenotificationapp.timerUtil.Actions
import com.example.servicenotificationapp.timerUtil.RESUME_VALUE

object TimerActionModule {

    fun provideClickPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationService::class.java).apply {
            action = Actions.RESUME.toString()
        }
        return PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun providePausePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationService::class.java).apply {
            action = Actions.PAUSE.toString()
        }
        return PendingIntent
            .getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun provideStopPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationService::class.java).apply {
            action = Actions.STOP.toString()
        }
        return PendingIntent
            .getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun provideResumePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationService::class.java).apply {
            action = Actions.RESUME.toString()
        }
        return PendingIntent
            .getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun triggerService(context: Context, actions: String, extraVal: Int? = null) {
        Intent(context, NotificationService::class.java).apply {
            action = actions
            if (extraVal != null) {
                putExtra(RESUME_VALUE, extraVal)
            }
            context.startService(this)
        }
    }
}