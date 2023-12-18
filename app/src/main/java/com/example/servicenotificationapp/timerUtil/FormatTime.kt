package com.example.servicenotificationapp.timerUtil

import com.example.servicenotificationapp.data.TimerState

fun formatTime(hourStr: String, minuteStr: String, secondStr: String): TimerState {
    val hour = hourStr.toIntOrNull() ?: 0
    val minute = minuteStr.toIntOrNull() ?: 0
    val second = secondStr.toIntOrNull() ?: 0

    val totalTimeInSeconds = hour * 3600 + minute * 60 + second

    val formattedHour = totalTimeInSeconds / 3600
    val formattedMinute = (totalTimeInSeconds % 3600) / 60
    val formattedSecond = totalTimeInSeconds % 60

    return TimerState(formattedHour, formattedMinute, formattedSecond)
}

fun timeToSeconds(timerState: TimerState): Int {
    return timerState.hours * 3600 + timerState.minutes * 60 + timerState.seconds
}

fun timerStateToFormat(timerState: TimerState): String {
    return timerState.hours.toString() +
            ":" + timerState.minutes.toString() +
            ":" + timerState.seconds.toString()
}

fun secondsToTimerState(totalSeconds: Int): TimerState {
    val hours = totalSeconds / 3600
    val remainingSecondsAfterHours = totalSeconds % 3600
    val minutes = remainingSecondsAfterHours / 60
    val seconds = remainingSecondsAfterHours % 60

    return TimerState(hours, minutes, seconds)
}

fun decrementing(timerState: TimerState): TimerState {
    return secondsToTimerState(
        timeToSeconds(
            timerState
        )
                - 1
    )
}

fun secondsToTimeString(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secondsRemaining = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secondsRemaining)
}
