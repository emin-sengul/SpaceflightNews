package com.emin.spaceflightnews.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

internal fun forwardEnterTransition(): EnterTransition =
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth / SLIDE_FRACTION },
        animationSpec = tween(NAV_ANIMATION_MILLIS),
    ) + fadeIn(animationSpec = tween(NAV_ANIMATION_MILLIS))

internal fun forwardExitTransition(): ExitTransition =
    scaleOut(targetScale = RECEDE_SCALE, animationSpec = tween(NAV_ANIMATION_MILLIS)) +
        fadeOut(animationSpec = tween(NAV_ANIMATION_MILLIS / 2))

internal fun backEnterTransition(): EnterTransition =
    scaleIn(initialScale = RECEDE_SCALE, animationSpec = tween(NAV_ANIMATION_MILLIS)) +
        fadeIn(animationSpec = tween(NAV_ANIMATION_MILLIS))

internal fun backExitTransition(): ExitTransition =
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth / SLIDE_FRACTION },
        animationSpec = tween(NAV_ANIMATION_MILLIS),
    ) + fadeOut(animationSpec = tween(NAV_ANIMATION_MILLIS))

private const val NAV_ANIMATION_MILLIS = 320
private const val SLIDE_FRACTION = 5
private const val RECEDE_SCALE = 0.97f
