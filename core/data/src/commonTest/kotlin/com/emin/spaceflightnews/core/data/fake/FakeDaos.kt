package com.emin.spaceflightnews.core.data.fake

import com.emin.spaceflightnews.core.database.dao.ArticleDao
import com.emin.spaceflightnews.core.database.dao.FavoriteDao
import com.emin.spaceflightnews.core.database.entity.CachedArticleEntity
import com.emin.spaceflightnews.core.database.entity.FavoriteArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class FakeArticleDao : ArticleDao() {

    private val rows = MutableStateFlow<List<CachedArticleEntity>>(emptyList())

    val current: List<CachedArticleEntity> get() = rows.value

    override fun observeAll(): Flow<List<CachedArticleEntity>> =
        rows.map { list -> list.sortedByDescending { it.article.publishedAtEpochMillis } }

    override fun observeById(id: Long): Flow<CachedArticleEntity?> =
        rows.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun count(): Int = rows.value.size

    override suspend fun upsertAll(articles: List<CachedArticleEntity>) {
        val merged = rows.value.toMutableList()
        articles.forEach { article ->
            merged.removeAll { it.id == article.id }
            merged += article
        }
        rows.value = merged
    }

    override suspend fun clear() {
        rows.value = emptyList()
    }
}

internal class FakeFavoriteDao : FavoriteDao {

    private val rows = MutableStateFlow<List<FavoriteArticleEntity>>(emptyList())

    val current: List<FavoriteArticleEntity> get() = rows.value

    override fun observeAll(): Flow<List<FavoriteArticleEntity>> =
        rows.map { list -> list.sortedByDescending { it.savedAtEpochMillis } }

    override fun observeIds(): Flow<List<Long>> = rows.map { list -> list.map { it.id } }

    override fun observeById(id: Long): Flow<FavoriteArticleEntity?> =
        rows.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun upsert(article: FavoriteArticleEntity) {
        rows.value = rows.value.filterNot { it.id == article.id } + article
    }

    override suspend fun deleteById(id: Long) {
        rows.value = rows.value.filterNot { it.id == id }
    }
}
