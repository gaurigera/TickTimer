package com.example.servicenotificationapp.timerServices.shared

import androidx.lifecycle.MutableLiveData

class TimerRepository : MutableLiveData<List<Int>>() {

    fun setTimerSeconds(seconds: List<Int>) {
        postValue(seconds)
        value = seconds
    }

    fun addTimerSecond(second: Int) : Int {
        val currentSeconds = value ?: emptyList()
        val updatedSeconds = currentSeconds.toMutableList().apply { add(second) }
        setTimerSeconds(updatedSeconds)
        return currentSeconds.size
    }

    fun removeTimerSecond(index: Int) {
        val currentSeconds = value ?: emptyList()
        if (index in currentSeconds.indices) {
            val updatedSeconds = currentSeconds.toMutableList().apply { removeAt(index) }
            setTimerSeconds(updatedSeconds)
        }
    }
}
