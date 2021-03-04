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

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class TimerViewModel : ViewModel() {
    private var isRunning = false
        set(value) {
            field = value
            updateTimerUIState()
        }

    private var totalTime = 0L
        set(value) {
            field = value
            updateTimerUIState()
        }

    private var remainingTime = 0L
        set(value) {
            field = value
            updateTimerUIState()
        }

    private var timer = getTimer()

    private val _timerUIState = MutableStateFlow(TimerUIState(totalTime, remainingTime, isRunning))
    val timerUIState: StateFlow<TimerUIState> = _timerUIState

    private fun updateTimerUIState() {
        _timerUIState.value = TimerUIState(totalTime, remainingTime, isRunning)
    }

    private fun getTimer() = object : CountDownTimer(totalTime * 1000, TICK_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            remainingTime = (millisUntilFinished / 1000.0).roundToLong()
        }

        override fun onFinish() {
            isRunning = false
        }
    }

    fun changeTotalTime(timeUnit: TimeUnit, changeDirection: ChangeDirection) {
        val multiplier = when (changeDirection) {
            ChangeDirection.Positive -> +1
            ChangeDirection.Negative -> -1
        }
        val newTime = totalTime + timeUnit.toSeconds(multiplier.toLong())
        totalTime = newTime.coerceIn(MIN_TOTAL_TIME, MAX_TOTAL_TIME)
    }

    fun start() {
        isRunning = true
        timer = getTimer().also { it.start() }
    }

    fun stop() {
        isRunning = false
        timer.cancel()
    }

    companion object {
        const val TICK_INTERVAL = 1000L

        const val MIN_TOTAL_TIME = 0L
        const val MAX_TOTAL_TIME = 86_400L // 24 hours
    }
}

enum class ChangeDirection {
    Positive,
    Negative
}

data class TimerUIState(
    val totalTime: Long,
    val remainingTime: Long,
    val isRunning: Boolean
)
