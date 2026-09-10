package com.emin.spaceflightnews.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNull
import com.emin.spaceflightnews.core.designsystem.component.EmptyState
import com.emin.spaceflightnews.core.designsystem.icon.SpaceflightIcons
import com.emin.spaceflightnews.feature.articles.ArticlesRoute
import com.emin.spaceflightnews.feature.detail.ArticleDetailRoute

@Composable
internal fun FeedPane(
    listState: LazyListState,
    onArticleClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        if (maxWidth < EXPANDED_WIDTH_BREAKPOINT) {
            ArticlesRoute(listState = listState, onArticleClick = onArticleClick)
            return@BoxWithConstraints
        }

        var selectedArticleId: Long? by rememberSaveable { mutableStateOf(null) }
        val listPaneWidth = maxWidth * LIST_PANE_FRACTION

        Row(modifier = Modifier.fillMaxSize()) {
            ArticlesRoute(
                listState = listState,
                onArticleClick = { selectedArticleId = it },
                modifier = Modifier.width(listPaneWidth),
            )

            VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Box(modifier = Modifier.fillMaxSize()) {
                Crossfade(
                    targetState = selectedArticleId,
                    animationSpec = tween(PANE_ANIMATION_MILLIS),
                    label = "detailPane",
                ) { articleId ->
                    if (articleId.isNull()) {
                        EmptyState(
                            title = "Nothing selected",
                            message = "Pick an article on the left to read it here.",
                            icon = SpaceflightIcons.Feed,
                        )
                    } else {
                        ArticleDetailRoute(articleId = articleId, onBack = null)
                    }
                }
            }
        }
    }
}

private const val PANE_ANIMATION_MILLIS = 320
private const val LIST_PANE_FRACTION = 0.42f
private val EXPANDED_WIDTH_BREAKPOINT = 840.dp
