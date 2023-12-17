package com.example.servicenotificationapp.data

data class TimerRowState(
    var timerState: TimerState,
    var isReset: Boolean,
    var isPaused: Boolean
)