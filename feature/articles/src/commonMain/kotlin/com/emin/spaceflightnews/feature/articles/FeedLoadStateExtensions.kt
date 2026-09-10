package com.emin.spaceflightnews.feature.articles

import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.domain.model.Article

internal fun FeedLoadState.loading(): FeedLoadState = copy(isLoading = true, error = null)

internal fun FeedLoadState.loaded(): FeedLoadState = copy(isLoading = false, error = null)

internal fun FeedLoadState.failed(error: AppError): FeedLoadState =
    copy(isLoading = false, error = error)

internal fun FeedLoadState.refreshing(): FeedLoadState = copy(isRefreshing = true, error = null)

internal fun FeedLoadState.refreshed(): FeedLoadState = copy(isRefreshing = false)

internal fun FeedLoadState.loadingMore(): FeedLoadState = copy(isLoadingMore = true)

internal fun FeedLoadState.loadedMore(): FeedLoadState = copy(isLoadingMore = false)

internal fun FeedLoadState.restartPaging(): FeedLoadState = copy(endReached = false, error = null)

internal fun FeedLoadState.startingSearch(): FeedLoadState =
    copy(isLoading = true, endReached = false, error = null)

internal fun FeedLoadState.pageLoaded(endReached: Boolean): FeedLoadState =
    copy(endReached = endReached, error = null)

internal fun FeedLoadState.clearError(): FeedLoadState = copy(error = null)

internal fun FeedLoadState.toUiState(query: String, articles: List<Article>): ArticlesUiState =
    ArticlesUiState(
        query = query,
        articles = articles,
        isLoading = isLoading && articles.isEmpty(),
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        canLoadMore = !endReached,
        blockingError = error?.takeIf { articles.isEmpty() },
        bannerError = error?.takeIf { articles.isNotEmpty() },
    )
