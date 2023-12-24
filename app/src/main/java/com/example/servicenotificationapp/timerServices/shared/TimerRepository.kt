package com.example.servicenotificationapp.timerServices.shared

import androidx.lifecycle.LiveData
import com.example.servicenotificationapp.data.TimerRowState
import com.example.servicenotificationapp.data.TimerState

object TimerRepository : LiveData<List<TimerRowState>>() {
    fun setTimerRepository(list: List<TimerRowState>) {
        postValue(list)
    }

    fun updateTimerRepository(index: Int, timerState: TimerState) {
        val currentList = value.orEmpty().toMutableList()
        currentList[index] = TimerRowState(timerState, false)
        postValue(currentList)
    }

    fun removeAtIndex(index: Int){
        val currentList = value.orEmpty().toMutableList()
        currentList.removeAt(index)
        postValue(currentList)
    }
}