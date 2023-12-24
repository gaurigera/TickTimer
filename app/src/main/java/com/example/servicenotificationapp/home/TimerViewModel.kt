package com.example.servicenotificationapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicenotificationapp.data.TimerRowState
import com.example.servicenotificationapp.timerServices.shared.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(val timerRepository: TimerRepository) : ViewModel() {
    private val _timerList = MutableStateFlow(mutableListOf<TimerRowState>())
    val timerList: StateFlow<List<TimerRowState>> get() = _timerList

    init {
        viewModelScope.launch {
            timerRepository.observeForever { newTimerList ->
                _timerList.value = newTimerList.toMutableList()
            }
        }
    }

    fun addTimer(timerRowState: TimerRowState) {
        _timerList.value = _timerList.value.toMutableList().apply { add(timerRowState) }
        timerRepository.setTimerRepository(timerList.value)
    }

    fun deleteTimer(position: Int) {
        _timerList.value = _timerList.value.toMutableList().apply { removeAt(position) }
        timerRepository.setTimerRepository(timerList.value)
    }

    fun updateTimerList(position: Int, value: TimerRowState) {
        _timerList.value[position] = value
        timerRepository.updateTimerRepository(position, value.timerState)
    }
}