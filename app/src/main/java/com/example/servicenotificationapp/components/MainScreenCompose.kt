package com.example.servicenotificationapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servicenotificationapp.R
import com.example.servicenotificationapp.data.TimerRowState
import com.example.servicenotificationapp.home.TimerViewModel
import com.example.servicenotificationapp.timerServices.TimerActionModule
import com.example.servicenotificationapp.timerUtil.Actions
import com.example.servicenotificationapp.timerUtil.timerStateToFormat

@Composable
fun MainScreen(
    viewModel: TimerViewModel
) {
    val timer by remember { viewModel.timerList }.collectAsState()

    LazyColumn {
        items(timer.size) { index ->
            println(index)
            TimerRow(
                timerRowState = timer[index],
                index = index,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun TimerRow(
    timerRowState: TimerRowState,
    index: Int,
    viewModel: TimerViewModel
) {
    var isPaused by remember(timerRowState.isPaused) {
        mutableStateOf(timerRowState.isPaused)
    }
    var timerState = timerRowState.timerState
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            timerStateToFormat(timerState),
            modifier = Modifier.background(Color.Transparent),
            color = Color.Black,
            fontSize = 44.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {
                isPaused = !isPaused
                if (!isPaused) {
                    TimerActionModule.triggerService(
                        context,
                        Actions.RESUME.toString(),
                        index
                    )
                } else {
                    TimerActionModule.triggerService(context, Actions.PAUSE.toString(), index)
                }

            }) {
                if (isPaused) {
                    Icon(Icons.Filled.PlayArrow, "Play")
                } else {
                    Icon(painterResource(id = R.drawable.baseline_pause_24), "Pause")
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))

            Button(onClick = {
//                viewModel.deleteTimer(index)
                TimerActionModule.triggerService(context, Actions.STOP.toString(), index)
            }) {
                Icon(Icons.Filled.Delete, "Delete timer")
            }
        }
    }
}

