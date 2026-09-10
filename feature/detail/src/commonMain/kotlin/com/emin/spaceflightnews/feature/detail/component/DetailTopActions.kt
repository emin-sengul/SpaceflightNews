package com.emin.spaceflightnews.feature.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.designsystem.component.FavoriteToggle
import com.emin.spaceflightnews.core.designsystem.icon.SpaceflightIcons

@Composable
internal fun DetailTopActions(
    isFavorite: Boolean?,
    onBack: (() -> Unit)?,
    onShare: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack.isNotNull()) {
            ActionScrim {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = SpaceflightIcons.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isFavorite.isNotNull()) {
            ActionScrim {
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = SpaceflightIcons.Share,
                        contentDescription = "Share article",
                        tint = Color.White,
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            ActionScrim {
                FavoriteToggle(
                    isFavorite = isFavorite,
                    onClick = onFavoriteToggle,
                    inactiveTint = Color.White,
                )
            }
        }
    }
}

@Composable
private fun ActionScrim(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(ScrimColor),
        content = { content() },
    )
}

private val ScrimColor = Color.Black.copy(alpha = 0.32f)
