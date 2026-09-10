package com.emin.spaceflightnews.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.designsystem.icon.SpaceflightIcons

@Composable
internal fun FloatingTabBar(
    isFeedSelected: Boolean,
    isFavoritesSelected: Boolean,
    onFeedClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 26.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(BAR_HEIGHT),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = BAR_OPACITY),
        shadowElevation = 12.dp,
        border = BorderStroke(
            width = Dp.Hairline,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TabItem(
                selectedIcon = SpaceflightIcons.Home,
                unselectedIcon = SpaceflightIcons.HomeBorder,
                contentDescription = "News",
                selected = isFeedSelected,
                onClick = onFeedClick,
            )
            TabItem(
                selectedIcon = SpaceflightIcons.Favorite,
                unselectedIcon = SpaceflightIcons.FavoriteBorder,
                contentDescription = "Favorites",
                selected = isFavoritesSelected,
                onClick = onFavoritesClick,
            )
        }
    }
}

private val BAR_HEIGHT = 58.dp
private const val BAR_OPACITY = 0.94f
