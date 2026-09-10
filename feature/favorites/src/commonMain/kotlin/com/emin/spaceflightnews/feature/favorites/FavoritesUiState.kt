package com.emin.spaceflightnews.feature.favorites

import androidx.compose.runtime.Immutable
import com.emin.spaceflightnews.core.domain.model.Article

@Immutable
data class FavoritesUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true,
) {
    val isEmpty: Boolean get() = articles.isEmpty() && !isLoading
}
