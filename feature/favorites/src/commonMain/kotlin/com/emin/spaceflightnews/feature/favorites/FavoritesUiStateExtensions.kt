package com.emin.spaceflightnews.feature.favorites

import com.emin.spaceflightnews.core.domain.model.Article

internal fun List<Article>.toFavoritesUiState(): FavoritesUiState =
    FavoritesUiState(articles = this, isLoading = false)

internal fun FavoritesUiState.subtitle(): String = when {
    isLoading -> "Loading your saved articles"
    articles.isEmpty() -> "Articles you keep for later live here"
    articles.size == 1 -> "1 article, available offline"
    else -> articles.size.toString() + " articles, available offline"
}
