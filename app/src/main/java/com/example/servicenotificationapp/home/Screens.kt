package com.example.servicenotificationapp.home

sealed class Screens(val route: String) {
    data object MainScreen : Screens("main_screen")
    data object DetailScreen : Screens("timer_screen")
    data object TimerFragment : Screens("set_timer_screen")
}