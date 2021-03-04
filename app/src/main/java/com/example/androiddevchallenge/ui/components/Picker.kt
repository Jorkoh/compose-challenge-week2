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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.timer.ChangeDirection

@Composable
fun Picker(
    indicator: String,
    isInEditMode: Boolean,
    value: Int,
    onValueChange: (ChangeDirection) -> Unit
) {
    Column(
        modifier = Modifier.width(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StepButton(
            direction = ChangeDirection.Positive,
            enabled = isInEditMode,
            onPressed = onValueChange
        )
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = indicator,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.alignByBaseline()
            )
        }
        StepButton(
            direction = ChangeDirection.Negative,
            enabled = isInEditMode,
            onPressed = onValueChange
        )
    }
}

@Composable
private fun StepButton(
    direction: ChangeDirection,
    enabled: Boolean,
    onPressed: (ChangeDirection) -> Unit
) {
    val animationAlignment = if (direction == ChangeDirection.Positive) {
        Alignment.Top
    } else {
        Alignment.Bottom
    }
    AnimatedVisibility(
        visible = enabled,
        enter = fadeIn() + expandVertically(expandFrom = animationAlignment),
        exit = fadeOut() + shrinkVertically(shrinkTowards = animationAlignment)
    ) {
        IconButton(
            onClick = { onPressed(direction) }
        ) {
            Icon(
                imageVector = when (direction) {
                    ChangeDirection.Positive -> Icons.Filled.KeyboardArrowUp
                    ChangeDirection.Negative -> Icons.Filled.KeyboardArrowDown
                },
                contentDescription = stringResource(
                    id = when (direction) {
                        ChangeDirection.Positive -> R.string.increase_button
                        ChangeDirection.Negative -> R.string.decrease_button
                    }
                )
            )
        }
    }
}
