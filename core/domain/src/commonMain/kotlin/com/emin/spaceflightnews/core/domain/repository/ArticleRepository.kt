package com.emin.spaceflightnews.core.domain.repository

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.model.PagedArticles
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun observeFeed(): Flow<List<Article>>

    suspend fun refreshFeed(): DataResult<Unit>

    suspend fun loadMoreFeed(): DataResult<Boolean>

    suspend fun searchArticles(query: String, offset: Int, limit: Int): DataResult<PagedArticles>

    fun observeArticle(id: Long): Flow<Article?>

    suspend fun fetchArticle(id: Long): DataResult<Article>

    fun observeFavorites(): Flow<List<Article>>

    suspend fun setFavorite(article: Article, isFavorite: Boolean)
}
