@file:OptIn(ExperimentalTime::class)

package com.emin.spaceflightnews.core.data.repository

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.common.DispatcherProvider
import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.common.map
import com.emin.spaceflightnews.core.data.mapper.toArticles
import com.emin.spaceflightnews.core.data.mapper.toCachedEntities
import com.emin.spaceflightnews.core.data.mapper.toCachedEntity
import com.emin.spaceflightnews.core.data.mapper.toDomain
import com.emin.spaceflightnews.core.data.mapper.toFavoriteEntity
import com.emin.spaceflightnews.core.data.mapper.toPagedArticles
import com.emin.spaceflightnews.core.database.dao.ArticleDao
import com.emin.spaceflightnews.core.database.dao.FavoriteDao
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.model.PagedArticles
import com.emin.spaceflightnews.core.domain.repository.ArticleRepository
import com.emin.spaceflightnews.core.network.api.SpaceflightNewsApi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class OfflineFirstArticleRepository(
    private val api: SpaceflightNewsApi,
    private val articleDao: ArticleDao,
    private val favoriteDao: FavoriteDao,
    private val dispatchers: DispatcherProvider,
) : ArticleRepository {

    private val feedCursor = FeedPagingCursor()

    override fun observeFeed(): Flow<List<Article>> =
        combine(articleDao.observeAll(), favoriteDao.observeIds()) { cached, favoriteIds ->
            cached.toArticles(favoriteIds.toSet())
        }.flowOn(dispatchers.io)

    override suspend fun refreshFeed(): DataResult<Unit> = withContext(dispatchers.io) {
        api.getArticles(limit = PAGE_SIZE, offset = 0).map { response ->
            articleDao.replaceAll(response.toCachedEntities())
            feedCursor.reset(loadedCount = response.results.size, totalCount = response.count)
        }
    }

    override suspend fun loadMoreFeed(): DataResult<Boolean> = withContext(dispatchers.io) {
        val cursor = feedCursor.snapshot()
        if (cursor.endReached) return@withContext DataResult.Success(true)

        api.getArticles(limit = PAGE_SIZE, offset = cursor.loadedCount).map { response ->
            articleDao.upsertAll(response.toCachedEntities())
            feedCursor.advance(
                offset = cursor.loadedCount,
                loadedCount = response.results.size,
                totalCount = response.count,
            )
        }
    }

    override suspend fun searchArticles(
        query: String,
        offset: Int,
        limit: Int,
    ): DataResult<PagedArticles> = withContext(dispatchers.io) {
        val favoriteIds = favoriteDao.favoriteIdSet()
        api.getArticles(limit = limit, offset = offset, search = query)
            .map { response -> response.toPagedArticles(favoriteIds) }
    }

    override fun observeArticle(id: Long): Flow<Article?> =
        combine(articleDao.observeById(id), favoriteDao.observeById(id)) { cached, favorite ->
            cached?.toDomain(isFavorite = favorite.isNotNull()) ?: favorite?.toDomain()
        }.flowOn(dispatchers.io)

    override suspend fun fetchArticle(id: Long): DataResult<Article> = withContext(dispatchers.io) {
        api.getArticle(id).map { dto ->
            articleDao.upsertAll(listOf(dto.toCachedEntity()))
            dto.toDomain(isFavorite = favoriteDao.isFavorite(id))
        }
    }

    override fun observeFavorites(): Flow<List<Article>> =
        favoriteDao.observeAll()
            .map { favorites -> favorites.toArticles() }
            .flowOn(dispatchers.io)

    override suspend fun setFavorite(article: Article, isFavorite: Boolean) {
        withContext(dispatchers.io) {
            if (isFavorite) {
                val savedAt = Clock.System.now().toEpochMilliseconds()
                favoriteDao.upsert(article.toFavoriteEntity(savedAt))
            } else {
                favoriteDao.deleteById(article.id)
            }
        }
    }

    companion object {
        const val PAGE_SIZE: Int = 20
    }
}
