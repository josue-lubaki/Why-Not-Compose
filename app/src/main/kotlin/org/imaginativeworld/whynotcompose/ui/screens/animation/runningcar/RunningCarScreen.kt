/*
 * Copyright 2021 Md. Mahmudul Hasan Shohag
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ------------------------------------------------------------------------
 *
 * Project: Why Not Compose!
 * Developed by: @ImaginativeShohag
 *
 * Md. Mahmudul Hasan Shohag
 * imaginativeshohag@gmail.com
 *
 * Source: https://github.com/ImaginativeShohag/Why-Not-Compose
 */

package org.imaginativeworld.whynotcompose.ui.screens.animation.runningcar

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.imaginativeworld.whynotcompose.R
import org.imaginativeworld.whynotcompose.base.models.isLight
import org.imaginativeworld.whynotcompose.base.utils.UIThemeController
import org.imaginativeworld.whynotcompose.common.compose.theme.AppTheme

enum class CarState {
    INITIAL,
    RUNNING,
    GONE
}

@Composable
fun RunningCarScreen() {
    val animState = remember { MutableStateFlow(CarState.INITIAL) }
    var isFirstTime by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            animState.value = CarState.INITIAL

            if (isFirstTime) {
                isFirstTime = false
            } else {
                delay(3000)
            }

            animState.value = CarState.RUNNING

            delay(3000)

            animState.value = CarState.GONE

            delay(2500)
        }
    }

    RunningCarScreenSkeleton(
        animState
    )
}

@PreviewLightDark
@Composable
private fun RunningCarScreenPreview() {
    AppTheme {
        RunningCarScreen()
    }
}

@Suppress("ktlint:compose:modifier-missing-check")
@Composable
fun RunningCarScreenSkeleton(
    animStateFlow: StateFlow<CarState>
) {
    val uiThemeMode by UIThemeController.uiThemeMode.collectAsState()
    val animState = animStateFlow.collectAsState()

    val animRotationZ by animateFloatAsState(
        targetValue = when (animState.value) {
            CarState.INITIAL -> 0f
            CarState.RUNNING -> 360f
            CarState.GONE -> 720f
        },
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        ),
        label = "Compose Logo"
    )

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .statusBarsPadding()
            .fillMaxSize()
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_jetpack_compose_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(128.dp)
                    .graphicsLayer {
                        rotationZ = animRotationZ
                    }
            )

            Column(
                Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier.wrapContentHeight(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    val transition = rememberInfiniteTransition()
                    val updateTransition =
                        updateTransition(animState.value, label = "fore&back-ground")

                    val animatePositionBackground by updateTransition.animateFloat(
                        label = "background",
                        transitionSpec = {
                            tween(
                                2000,
                                easing = CubicBezierEasing(0.0f, 0.0f, 0.5f, 1.0f)
                            )
                        }
                    ) { state ->
                        when (state) {
                            // true -> -400f
                            // else -> 0f
                            CarState.INITIAL -> 0f
                            CarState.RUNNING -> -400f
                            CarState.GONE -> -800f
                        }
                    }

                    val animatePositionForeground by updateTransition.animateFloat(
                        label = "foreground",
                        transitionSpec = {
                            tween(
                                2000,
                                easing = CubicBezierEasing(0.0f, 0.0f, 0.5f, 1.0f)
                            )
                        }
                    ) { state ->
                        when (state) {
                            // true -> -700f
                            // else -> 0f

                            CarState.INITIAL -> 0f
                            CarState.RUNNING -> -700f
                            CarState.GONE -> -1400f
                        }
                    }

                    val animateCarPositionX by updateTransition.animateFloat(
                        label = "Car X position",
                        transitionSpec = {
                            tween(
                                2000,
                                easing = CubicBezierEasing(0.0f, 0.0f, 0.5f, 1.0f)
                            )
                        }
                    ) { state ->
                        when (state) {
                            // true -> 0f
                            // else -> -1000f

                            CarState.INITIAL -> -1000f
                            CarState.RUNNING -> 0f
                            CarState.GONE -> 1000f
                        }
                    }

                    val animatePositionCar by transition.animateFloat(
                        initialValue = 3f,
                        targetValue = -3f,
                        animationSpec = infiniteRepeatable(tween(300), RepeatMode.Reverse),
                        label = "Car"
                    )

                    Image(
                        painterResource(id = R.drawable.scrolling_background),
                        "background",
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 32.dp)
                            .height(48.dp)
                            .requiredWidth(1280.dp)
                            .graphicsLayer {
                                translationX = animatePositionBackground
                            },
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(
                            if (uiThemeMode.isLight()) {
                                Color(0xFFE6E6E6)
                            } else {
                                Color(0xFF2B2B2B)
                            }
                        )
                    )

                    Image(
                        painterResource(id = R.drawable.scrolling_foreground),
                        "foreground",
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 28.dp)
                            .height(48.dp)
                            .requiredWidth(1600.dp)
                            .graphicsLayer {
                                translationX = animatePositionForeground
                            },
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(
                            if (uiThemeMode.isLight()) {
                                Color(0xFFD4D4D4)
                            } else {
                                Color(0xFF3D3D3D)
                            }
                        )
                    )

                    Image(
                        painterResource(id = R.drawable.ic_car),
                        contentDescription = "Car",
                        Modifier
                            .padding(bottom = 0.dp)
                            .size(64.dp)
                            .graphicsLayer {
                                translationX = animateCarPositionX
                                translationY = animatePositionCar
                            }
                    )
                }
            }
        }
    }
}
