package com.emin.spaceflightnews.feature.detail.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.common.orFallback
import com.emin.spaceflightnews.core.common.takeIfNotBlank
import com.emin.spaceflightnews.core.designsystem.component.SourcePill
import com.emin.spaceflightnews.core.designsystem.icon.SpaceflightIcons
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.model.sourceUrl

@Composable
internal fun ArticleDetailContent(
    article: Article,
    onOpenSource: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appear = remember { Animatable(0f) }
    LaunchedEffect(article.id) {
        appear.snapTo(0f)
        appear.animateTo(1f, animationSpec = tween(durationMillis = APPEAR_MILLIS))
    }

    val metaLabel = remember(article) { article.metaLabel() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        ArticleHero(imageUrl = article.imageUrl)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = SHEET_OVERLAP),
            shape = RoundedCornerShape(topStart = SHEET_RADIUS, topEnd = SHEET_RADIUS),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .padding(top = 24.dp, bottom = 40.dp)
                    .alpha(appear.value),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                SourcePill(text = article.newsSite)

                Text(
                    text = article.title.orFallback(UNTITLED_ARTICLE),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    text = metaLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                article.summary.takeIfNotBlank()?.let { summary ->
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                if (article.sourceUrl.isNotNull()) {
                    FilledTonalButton(
                        onClick = onOpenSource,
                        modifier = Modifier.padding(top = 6.dp),
                    ) {
                        Icon(
                            imageVector = SpaceflightIcons.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = READ_MORE_PREFIX + article.newsSite.orFallback(UNKNOWN_SOURCE),
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                }
            }
        }
    }
}

private const val UNTITLED_ARTICLE = "Untitled article"
private const val UNKNOWN_SOURCE = "the source"
private const val READ_MORE_PREFIX = "Read the full story on "
private const val APPEAR_MILLIS = 420
private val SHEET_RADIUS = 28.dp
private val SHEET_OVERLAP = (-28).dp
