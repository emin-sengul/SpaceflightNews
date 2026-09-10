package com.emin.spaceflightnews.feature.articles

import com.emin.spaceflightnews.core.domain.model.Article

sealed interface ArticlesIntent {
    data class QueryChanged(val query: String) : ArticlesIntent
    data object Refresh : ArticlesIntent
    data object Retry : ArticlesIntent
    data object LoadMore : ArticlesIntent
    data class FavoriteToggled(val article: Article) : ArticlesIntent
    data object BannerDismissed : ArticlesIntent
}
