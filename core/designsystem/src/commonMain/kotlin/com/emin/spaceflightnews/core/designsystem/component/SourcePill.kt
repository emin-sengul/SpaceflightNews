package com.emin.spaceflightnews.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNotNullOrBlank

@Composable
fun SourcePill(
    text: String?,
    modifier: Modifier = Modifier,
) {
    if (text.isNotNullOrBlank()) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(PILL_RADIUS_PERCENT),
            color = MaterialTheme.colorScheme.surface.copy(alpha = PILL_OPACITY),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            )
        }
    }
}

private const val PILL_RADIUS_PERCENT = 50
private const val PILL_OPACITY = 0.88f
