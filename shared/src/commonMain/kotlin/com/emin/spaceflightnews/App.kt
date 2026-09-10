package com.emin.spaceflightnews

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.emin.spaceflightnews.core.designsystem.component.SplashScreen
import com.emin.spaceflightnews.core.designsystem.theme.SpaceflightNewsTheme
import com.emin.spaceflightnews.ui.MainScaffold
import kotlinx.coroutines.delay

@Composable
fun SpaceflightNewsApp() {
    SpaceflightNewsTheme {
        var isSplashVisible by rememberSaveable { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            delay(SPLASH_DURATION_MILLIS)
            isSplashVisible = false
        }

        Crossfade(
            targetState = isSplashVisible,
            animationSpec = tween(durationMillis = SPLASH_FADE_MILLIS),
            label = "splash",
        ) { showSplash ->
            if (showSplash) SplashScreen() else MainScaffold()
        }
    }
}

private const val SPLASH_DURATION_MILLIS = 1500L
private const val SPLASH_FADE_MILLIS = 500
