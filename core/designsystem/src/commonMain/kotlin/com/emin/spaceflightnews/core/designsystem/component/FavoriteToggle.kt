package com.emin.spaceflightnews.core.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.designsystem.icon.SpaceflightIcons

@Composable
fun FavoriteToggle(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    inactiveTint: Color? = null,
) {
    val pulse = remember { Animatable(NORMAL_SCALE) }

    LaunchedEffect(isFavorite) {
        if (!isFavorite) return@LaunchedEffect
        pulse.animateTo(PULSE_SCALE, animationSpec = tween(durationMillis = PULSE_IN_MILLIS))
        pulse.animateTo(NORMAL_SCALE, animationSpec = tween(durationMillis = PULSE_OUT_MILLIS))
    }

    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) SpaceflightIcons.Favorite else SpaceflightIcons.FavoriteBorder,
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
            tint = when {
                isFavorite -> MaterialTheme.colorScheme.tertiary
                inactiveTint.isNotNull() -> inactiveTint
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier
                .size(22.dp)
                .scale(pulse.value),
        )
    }
}

private const val NORMAL_SCALE = 1f
private const val PULSE_SCALE = 1.32f
private const val PULSE_IN_MILLIS = 130
private const val PULSE_OUT_MILLIS = 190
