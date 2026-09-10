package com.emin.spaceflightnews.feature.detail

sealed interface ArticleDetailIntent {
    data object FavoriteToggled : ArticleDetailIntent
    data object Retry : ArticleDetailIntent
    data object OpenSource : ArticleDetailIntent
    data object Share : ArticleDetailIntent
}
