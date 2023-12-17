package com.example.servicenotificationapp.home

import androidx.lifecycle.ViewModel
import com.example.servicenotificationapp.data.TimerRowState
import com.example.servicenotificationapp.data.TimerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerViewModel : ViewModel() {
    private val _timerList = MutableStateFlow(mutableListOf<TimerRowState>())
    val timerList: StateFlow<List<TimerRowState>> get() = _timerList


    fun addTimer(timerRowState: TimerRowState) {
        _timerList.value = _timerList.value.toMutableList().apply { add(timerRowState) }
    }

    fun deleteTimer(position: Int) {
        _timerList.value = _timerList.value.toMutableList().apply { removeAt(position) }
    }

    fun updateTimerList(position: Int, value: TimerRowState) {
        _timerList.value[position] = value
    }

    fun getTimerValue(position: Int): TimerState {
        return _timerList.value[position].timerState
    }
}