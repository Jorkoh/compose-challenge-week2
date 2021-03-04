/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.timer

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.components.Picker
import com.example.androiddevchallenge.ui.components.TimerCircle
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import java.util.concurrent.TimeUnit

@Composable
fun TimerScreen() {
    val timerViewModel: TimerViewModel = viewModel()

    TimerContent(
        timerUIState = timerViewModel.timerUIState.collectAsState(),
        onStartPressed = { timerViewModel.start() },
        onStopPressed = { timerViewModel.stop() },
        onValueChange = { timeUnit, changeDirection ->
            timerViewModel.changeTotalTime(timeUnit, changeDirection)
        }
    )
}

@Composable
fun TimerContent(
    timerUIState: State<TimerUIState>,
    onStartPressed: () -> Unit,
    onStopPressed: () -> Unit,
    onValueChange: (TimeUnit, ChangeDirection) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .aspectRatio(1f)
        ) {
            TimerCircle(timerUIState)
            TimePickers(
                state = timerUIState,
                modifier = Modifier.align(Alignment.Center),
                onValueChange = onValueChange
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        StartStopButton(
            timerUIState = timerUIState,
            onStopPressed = onStopPressed,
            onStartPressed = onStartPressed
        )
    }
}

@Composable
fun TimePickers(
    state: State<TimerUIState>,
    modifier: Modifier,
    onValueChange: (TimeUnit, ChangeDirection) -> Unit
) {
    Row(modifier = modifier) {
        val timeToRepresent = if (state.value.isRunning) {
            state.value.remainingTime
        } else {
            state.value.totalTime
        }

        Picker(
            indicator = "h",
            isInEditMode = !state.value.isRunning,
            value = TimeUnit.SECONDS.toHours(timeToRepresent).toInt(),
            onValueChange = { direction -> onValueChange(TimeUnit.HOURS, direction) }
        )
        Picker(
            indicator = "m",
            isInEditMode = !state.value.isRunning,
            value = TimeUnit.SECONDS.toMinutes(timeToRepresent).toInt() % 60,
            onValueChange = { direction -> onValueChange(TimeUnit.MINUTES, direction) }
        )
        Picker(
            indicator = "s",
            isInEditMode = !state.value.isRunning,
            value = TimeUnit.SECONDS.toSeconds(timeToRepresent).toInt() % 60,
            onValueChange = { direction -> onValueChange(TimeUnit.SECONDS, direction) }
        )
    }
}

@Composable
private fun StartStopButton(
    timerUIState: State<TimerUIState>,
    onStopPressed: () -> Unit,
    onStartPressed: () -> Unit
) {
    Crossfade(targetState = timerUIState.value.isRunning) { isRunning ->
        when (isRunning) {
            true -> Button(onClick = { onStopPressed() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_stop),
                    contentDescription = stringResource(R.string.stop_button),
                    modifier = Modifier.size(36.dp)
                )
            }
            false -> Button(onClick = { onStartPressed() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = stringResource(R.string.play_button),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
