package com.emin.spaceflightnews.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.emin.spaceflightnews.core.designsystem.component.AttributionFooter
import com.emin.spaceflightnews.core.designsystem.component.EmptyState
import com.emin.spaceflightnews.core.designsystem.component.LoadingState
import com.emin.spaceflightnews.core.designsystem.icon.SpaceflightIcons
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.ui.ArticleListDefaults
import com.emin.spaceflightnews.core.ui.articleItems
import com.emin.spaceflightnews.core.ui.footerItem
import com.emin.spaceflightnews.feature.favorites.component.FavoritesHeader

@Composable
fun FavoritesScreen(
    state: FavoritesUiState,
    onIntent: (FavoritesIntent) -> Unit,
    onArticleClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val onRemove = remember(onIntent) {
        { article: Article -> onIntent(FavoritesIntent.FavoriteRemoved(article)) }
    }

    Column(modifier = modifier.fillMaxSize()) {
        FavoritesHeader(subtitle = state.subtitle())

        when {
            state.isLoading -> LoadingState()

            state.isEmpty -> EmptyState(
                title = EMPTY_TITLE,
                message = EMPTY_MESSAGE,
                icon = SpaceflightIcons.FavoriteBorder,
            )

            else -> LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = ArticleListDefaults.contentPadding,
                verticalArrangement = Arrangement.spacedBy(ArticleListDefaults.itemSpacing),
            ) {
                articleItems(
                    articles = state.articles,
                    onArticleClick = onArticleClick,
                    onFavoriteClick = onRemove,
                )

                footerItem(key = ATTRIBUTION_KEY) { AttributionFooter() }
            }
        }
    }
}

private const val ATTRIBUTION_KEY = "favorites-attribution"
private const val EMPTY_TITLE = "No favorites yet"
private const val EMPTY_MESSAGE =
    "Tap the heart on any article to keep it here, available even offline."
