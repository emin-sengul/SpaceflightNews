package com.emin.spaceflightnews.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.emin.spaceflightnews.core.database.entity.CachedArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ArticleDao {
    @Query("SELECT * FROM cached_articles ORDER BY publishedAtEpochMillis DESC")
    abstract fun observeAll(): Flow<List<CachedArticleEntity>>

    @Query("SELECT * FROM cached_articles WHERE id = :id")
    abstract fun observeById(id: Long): Flow<CachedArticleEntity?>

    @Query("SELECT COUNT(*) FROM cached_articles")
    abstract suspend fun count(): Int

    @Upsert
    abstract suspend fun upsertAll(articles: List<CachedArticleEntity>)

    @Query("DELETE FROM cached_articles")
    abstract suspend fun clear()

    @Transaction
    open suspend fun replaceAll(articles: List<CachedArticleEntity>) {
        clear()
        upsertAll(articles)
    }
}
