package com.emin.spaceflightnews.feature.articles

import androidx.compose.runtime.Immutable
import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.common.isNull
import com.emin.spaceflightnews.core.domain.model.Article

@Immutable
data class ArticlesUiState(
    val query: String = "",
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val blockingError: AppError? = null,
    val bannerError: AppError? = null,
) {
    val isSearchActive: Boolean get() = query.isNotBlank()

    val isEmpty: Boolean get() = articles.isEmpty() && !isLoading && blockingError.isNull()
}
