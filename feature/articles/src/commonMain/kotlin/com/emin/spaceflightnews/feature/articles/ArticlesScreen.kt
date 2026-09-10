@file:OptIn(ExperimentalMaterial3Api::class)

package com.emin.spaceflightnews.feature.articles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.designsystem.component.EmptyState
import com.emin.spaceflightnews.core.designsystem.component.ErrorBanner
import com.emin.spaceflightnews.core.designsystem.component.ErrorState
import com.emin.spaceflightnews.core.designsystem.component.LoadingState
import com.emin.spaceflightnews.core.designsystem.util.toUserMessage
import com.emin.spaceflightnews.core.designsystem.util.toUserTitle
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.feature.articles.component.ArticleList
import com.emin.spaceflightnews.feature.articles.component.HeaderedMessage
import com.emin.spaceflightnews.feature.articles.effect.InfiniteScrollEffect

@Composable
fun ArticlesScreen(
    state: ArticlesUiState,
    onIntent: (ArticlesIntent) -> Unit,
    onArticleClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val onQueryChange = remember(onIntent) {
        { query: String -> onIntent(ArticlesIntent.QueryChanged(query)) }
    }
    val onFavoriteClick = remember(onIntent) {
        { article: Article -> onIntent(ArticlesIntent.FavoriteToggled(article)) }
    }

    Column(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.bannerError.isNotNull(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            ErrorBanner(
                message = state.bannerError?.toUserMessage().orEmpty(),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            )
        }

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onIntent(ArticlesIntent.Refresh) },
            modifier = Modifier.fillMaxSize(),
        ) {
            when {
                state.isLoading -> HeaderedMessage(state.query, onQueryChange) {
                    LoadingState()
                }

                state.blockingError.isNotNull() -> HeaderedMessage(state.query, onQueryChange) {
                    ErrorState(
                        title = state.blockingError.toUserTitle(),
                        message = state.blockingError.toUserMessage(),
                        onRetry = { onIntent(ArticlesIntent.Retry) },
                    )
                }

                state.isEmpty -> HeaderedMessage(state.query, onQueryChange) {
                    EmptyState(
                        title = state.emptyTitle(),
                        message = state.emptyMessage(),
                        icon = state.emptyIcon(),
                    )
                }

                else -> ArticleList(
                    articles = state.articles,
                    query = state.query,
                    onQueryChange = onQueryChange,
                    isLoadingMore = state.isLoadingMore,
                    showAttribution = state.showsAttribution,
                    listState = listState,
                    onArticleClick = onArticleClick,
                    onFavoriteClick = onFavoriteClick,
                )
            }
        }
    }

    InfiniteScrollEffect(
        listState = listState,
        itemCount = state.articles.size,
        enabled = state.canPageFurther,
        onLoadMore = { onIntent(ArticlesIntent.LoadMore) },
    )
}
