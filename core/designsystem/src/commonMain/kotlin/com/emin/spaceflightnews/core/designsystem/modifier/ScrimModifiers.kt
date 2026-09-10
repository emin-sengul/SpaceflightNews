package com.emin.spaceflightnews.core.designsystem.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

fun Modifier.scrim(brush: Brush): Modifier = fillMaxSize().background(brush)
