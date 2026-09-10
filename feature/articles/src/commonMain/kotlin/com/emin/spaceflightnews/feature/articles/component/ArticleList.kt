package com.emin.spaceflightnews.feature.articles.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.emin.spaceflightnews.core.designsystem.component.AttributionFooter
import com.emin.spaceflightnews.core.designsystem.component.LoadMoreIndicator
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.ui.ArticleListDefaults
import com.emin.spaceflightnews.core.ui.articleItems
import com.emin.spaceflightnews.core.ui.footerItem
import com.emin.spaceflightnews.core.ui.headerItem

@Composable
internal fun ArticleList(
    articles: List<Article>,
    query: String,
    onQueryChange: (String) -> Unit,
    isLoadingMore: Boolean,
    showAttribution: Boolean,
    listState: LazyListState,
    onArticleClick: (Long) -> Unit,
    onFavoriteClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = ArticleListDefaults.contentPadding,
        verticalArrangement = Arrangement.spacedBy(ArticleListDefaults.itemSpacing),
    ) {
        headerItem(key = FEED_HEADER_KEY) {
            FeedHeader(query = query, onQueryChange = onQueryChange)
        }

        articleItems(
            articles = articles,
            onArticleClick = onArticleClick,
            onFavoriteClick = onFavoriteClick,
        )

        if (isLoadingMore) {
            footerItem(key = LOAD_MORE_KEY) { LoadMoreIndicator() }
        }

        if (showAttribution) {
            footerItem(key = ATTRIBUTION_KEY) { AttributionFooter() }
        }
    }
}

private const val FEED_HEADER_KEY = "feed-header"
private const val LOAD_MORE_KEY = "feed-load-more"
private const val ATTRIBUTION_KEY = "feed-attribution"
