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
package com.example.androiddevchallenge.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.graphCircle
import com.example.androiddevchallenge.ui.timer.TimerUIState

@Composable
fun TimerCircle(timerUIState: State<TimerUIState>) {

    val state = timerUIState.value
    val sweepAngle: Float by animateFloatAsState(
        targetValue = when (state.isRunning) {
            true -> state.remainingTime.toFloat() / state.totalTime
            false -> 0f
        },
        animationSpec = spring(stiffness = 1000f)
    )
    val graphCircleColor = MaterialTheme.colors.graphCircle
    val graphArcColor = MaterialTheme.colors.primary

    Canvas(modifier = Modifier.fillMaxSize()) {

        drawCircle(
            color = graphCircleColor,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = graphArcColor,
            style = Stroke(width = 4.dp.toPx()),
            startAngle = -90f,
            sweepAngle = sweepAngle * 360,
            useCenter = false
        )
    }
}
