package com.example.servicenotificationapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.servicenotificationapp.components.MainScreen
import com.example.servicenotificationapp.components.TimerFragment
import com.example.servicenotificationapp.home.TimerViewModel
import com.example.servicenotificationapp.ui.theme.ServiceNotificationAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: TimerViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val permission: PermissionState =
                rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
            ServiceNotificationAppTheme(
                isSystemInDarkTheme(), false
            ) {
                Scaffold(
                    topBar = { AppBar() },
                    floatingActionButton = { FloatingAddActionButton() }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = {
            Text("Timer")
        }
    )
}

//@Preview
@Composable
fun FloatingAddActionButton() {
    var expanded by remember { mutableStateOf(true) }
    var submitted by remember { mutableStateOf(false) }

    DisposableEffect(submitted) {
        onDispose {
            expanded = false
            submitted = false
        }
    }

    FloatingActionButton(
        onClick = {
            expanded = !expanded
        },
        containerColor = Color.DarkGray
    ) {
        AnimatedContent(targetState = expanded, transitionSpec = {
            fadeIn(animationSpec = tween(150, 150)) togetherWith
                    fadeOut(animationSpec = tween(150)) using
                    SizeTransform { initialSize, targetSize ->
                        if (targetState) {
                            keyframes {
                                IntSize(targetSize.width, initialSize.height) at 150
                                durationMillis = 300
                            }
                        } else {
                            keyframes {
                                IntSize(initialSize.width, targetSize.height) at 150
                                durationMillis = 300
                            }
                        }
                    }
        }, label = "") { targetState ->
            if (targetState) {
                Box(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Icon(Icons.Filled.ArrowBack, "")
                    TimerFragment {
                        submitted = true
                    }
                }
            } else {
                Icon(Icons.Filled.Add, "")
            }
        }
    }
}