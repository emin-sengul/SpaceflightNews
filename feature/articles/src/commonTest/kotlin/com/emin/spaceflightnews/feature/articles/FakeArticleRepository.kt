@file:OptIn(ExperimentalTime::class)

package com.emin.spaceflightnews.feature.articles

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.model.PagedArticles
import com.emin.spaceflightnews.core.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal class FakeArticleRepository : ArticleRepository {
    val feed = MutableStateFlow<List<Article>>(emptyList())
    val favorites = MutableStateFlow<List<Article>>(emptyList())

    var refreshBehaviour: () -> DataResult<Unit> = { DataResult.Success(Unit) }
    var loadMoreResult: DataResult<Boolean> = DataResult.Success(false)
    var searchResult: DataResult<PagedArticles> = DataResult.Success(PagedArticles(emptyList(), 0))

    var refreshCount = 0
        private set
    var loadMoreCount = 0
        private set
    var lastSearchQuery: String? = null
        private set
    var lastSearchOffset: Int = -1
        private set
    val favoriteChanges = mutableListOf<Pair<Long, Boolean>>()

    override fun observeFeed(): Flow<List<Article>> = feed

    override suspend fun refreshFeed(): DataResult<Unit> {
        refreshCount++
        return refreshBehaviour()
    }

    override suspend fun loadMoreFeed(): DataResult<Boolean> {
        loadMoreCount++
        return loadMoreResult
    }

    override suspend fun searchArticles(
        query: String,
        offset: Int,
        limit: Int,
    ): DataResult<PagedArticles> {
        lastSearchQuery = query
        lastSearchOffset = offset
        return searchResult
    }

    override fun observeArticle(id: Long): Flow<Article?> =
        feed.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun fetchArticle(id: Long): DataResult<Article> =
        feed.value.firstOrNull { it.id == id }
            ?.let { DataResult.Success(it) }
            ?: DataResult.Failure(com.emin.spaceflightnews.core.common.AppError.ClientError(404))

    override fun observeFavorites(): Flow<List<Article>> = favorites

    override suspend fun setFavorite(article: Article, isFavorite: Boolean) {
        favoriteChanges += article.id to isFavorite
        favorites.value = if (isFavorite) {
            favorites.value + article.copy(isFavorite = true)
        } else {
            favorites.value.filterNot { it.id == article.id }
        }
    }
}

internal fun testArticle(
    id: Long,
    title: String = "Article $id",
    isFavorite: Boolean = false,
): Article = Article(
    id = id,
    title = title,
    summary = "Summary $id",
    imageUrl = null,
    newsSite = "Space Scout",
    url = "https://example.com/$id",
    publishedAt = Instant.parse("2026-09-01T10:00:00Z"),
    updatedAt = null,
    authors = listOf("Reporter"),
    isFavorite = isFavorite,
)
