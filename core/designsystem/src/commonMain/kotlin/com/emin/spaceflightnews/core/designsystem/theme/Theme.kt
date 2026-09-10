package com.emin.spaceflightnews.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SpaceflightNewsTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) SpaceflightDarkColorScheme else SpaceflightLightColorScheme,
        typography = SpaceflightTypography,
        content = content,
    )
}
