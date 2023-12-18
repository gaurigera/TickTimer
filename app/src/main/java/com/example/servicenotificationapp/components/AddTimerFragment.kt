package com.example.servicenotificationapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.servicenotificationapp.data.TimerRowState
import com.example.servicenotificationapp.home.TimerViewModel
import com.example.servicenotificationapp.timerUtil.formatTime

@Composable
fun TimerFragment(onSubmit: () -> Unit) {
    val hourState = rememberSaveable { mutableStateOf("00") }
    val minuteState = rememberSaveable { mutableStateOf("00") }
    val secondState = rememberSaveable { mutableStateOf("00") }

    val viewModel = viewModel<TimerViewModel>()

    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerText(
                hourState = hourState.value,
                minuteState = minuteState.value,
                secondState = secondState.value
            )
            NumberRow(
                numbers = listOf(
                    "7",
                    "8",
                    "9",
                    "4",
                    "5",
                    "6",
                    "1",
                    "2",
                    "3",
                    "00",
                    "0",
                    "Del"
                ), hourState.value + minuteState.value + secondState.value
            ) {
                hourState.value = it.substring(0, 2)
                minuteState.value = it.substring(2, 4)
                secondState.value = it.substring(4, 6)
            }
            Button(
                shape = RectangleShape, modifier = Modifier.padding(4.dp),onClick = {
                    viewModel.addTimer(
                        TimerRowState(
                            formatTime(
                                hourState.value,
                                minuteState.value,
                                secondState.value
                            ), true
                        )
                    )
                    numbersInserted = 0
                    onSubmit()
                }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Timer")
            }
        }
    }
}

@Composable
fun TimerText(
    hourState: String,
    minuteState: String,
    secondState: String
) {
    val timeSize = 44.sp
    val intermediateSize = 24.sp

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text = hourState,
            style = TextStyle(fontSize = timeSize)
        )
        Text(
            text = "h ",
            style = TextStyle(fontSize = intermediateSize)
        )

        Text(
            text = minuteState,
            style = TextStyle(fontSize = timeSize)
        )
        Text(
            text = "m ",
            style = TextStyle(fontSize = intermediateSize)
        )

        Text(
            text = secondState,
            style = TextStyle(fontSize = timeSize)
        )
        Text(
            text = "s", style = TextStyle(fontSize = intermediateSize)
        )
    }
}

@Composable
fun NumberRow(
    numbers: List<String>,
    timerText: String,
    stringState: (String) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.width(300.dp)) {
        items(numbers.size) {
            MyButton(text = numbers[it], timerText) { str ->
                stringState(str)
            }
        }
    }
}

var numbersInserted = 0
const val maxSize = 6

@Composable
fun MyButton(
    text: String,
    timerText: String,
    stringState: (String) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(3.dp),
        shape = RectangleShape,
        onClick = {
            when (text) {
                "Del" -> {
                    if (numbersInserted != 0) {
                        stringState(handleDeletion(timerText))
                    }
                }

                "00" -> {
                    stringState(handleSkip(timerText))
                }

                else -> {
                    if (numbersInserted <= 5) {
                        stringState(handleInsertion(timerText, text))
                    }
                }
            }
        }
    ) {
        Text(
            text,
            style = TextStyle(
                fontSize = 28.sp
            )
        )
    }
}

fun handleInsertion(str: String, btnText: String): String {
    if (numbersInserted + 1 > maxSize) return str
    numbersInserted += 1
    return str.substring(1) + btnText
}

fun handleDeletion(str: String): String {
    numbersInserted -= 1
    return "0" + str.dropLast(1)
}

fun handleSkip(str: String): String {
    if (numbersInserted + 2 >= maxSize) return str
    numbersInserted += 2
    return str.substring(2) + "00"
}


