package com.emin.spaceflightnews.feature.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.designsystem.component.ArticleImage
import com.emin.spaceflightnews.core.designsystem.modifier.scrim
import com.emin.spaceflightnews.core.designsystem.theme.SpaceflightBrand

@Composable
internal fun ArticleHero(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(HERO_HEIGHT),
    ) {
        if (imageUrl.isNotNull()) {
            ArticleImage(
                imageUrl = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Box(modifier = Modifier.scrim(SpaceflightBrand.gradient))
        }

        Box(modifier = Modifier.scrim(HeroScrim))
    }
}

internal val HERO_HEIGHT = 300.dp

private val HeroScrim = Brush.verticalGradient(
    colorStops = arrayOf(
        0f to Color.Black.copy(alpha = 0.45f),
        0.35f to Color.Transparent,
        1f to Color.Black.copy(alpha = 0.20f),
    ),
)

