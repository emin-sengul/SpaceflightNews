package com.emin.spaceflightnews.core.domain.model

data class PagedArticles(
    val articles: List<Article>,
    val totalCount: Int,
) {
    fun endReached(loadedCount: Int): Boolean = loadedCount >= totalCount
}
