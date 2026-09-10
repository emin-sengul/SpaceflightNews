package com.emin.spaceflightnews.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNotNullOrBlank
import com.emin.spaceflightnews.core.common.orFallback
import com.emin.spaceflightnews.core.common.takeIfNotBlank
import com.emin.spaceflightnews.core.designsystem.modifier.scrim

@Composable
fun ArticleCard(
    title: String,
    summary: String,
    newsSite: String,
    publishedLabel: String,
    imageUrl: String?,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
    ) {
        Column {
            if (imageUrl.isNotNullOrBlank()) {
                ArticleCardHeader(imageUrl = imageUrl, newsSite = newsSite)
            }

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 6.dp, top = 14.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (imageUrl.isNullOrBlank()) {
                    SourcePill(text = newsSite)
                }

                Text(
                    text = title.orFallback(UNTITLED_ARTICLE),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = TITLE_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 10.dp),
                )

                summary.takeIfNotBlank()?.let { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = SUMMARY_MAX_LINES,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 10.dp),
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = publishedLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                    )

                    FavoriteToggle(isFavorite = isFavorite, onClick = onFavoriteClick)
                }
            }
        }
    }
}

@Composable
private fun ArticleCardHeader(
    imageUrl: String,
    newsSite: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IMAGE_HEIGHT)
            .clip(RoundedCornerShape(topStart = CARD_RADIUS, topEnd = CARD_RADIUS)),
    ) {
        ArticleImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )

        Box(modifier = Modifier.scrim(ImageScrim))

        SourcePill(
            text = newsSite,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
        )
    }
}

private const val UNTITLED_ARTICLE = "Untitled article"
private const val TITLE_MAX_LINES = 3
private const val SUMMARY_MAX_LINES = 2
private val CARD_RADIUS = 20.dp
private val IMAGE_HEIGHT = 190.dp
private val CardShape = RoundedCornerShape(CARD_RADIUS)

private val ImageScrim = Brush.verticalGradient(
    colorStops = arrayOf(
        0.55f to Color.Transparent,
        1f to Color.Black.copy(alpha = 0.45f),
    ),
)
