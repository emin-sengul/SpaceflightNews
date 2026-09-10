package com.emin.spaceflightnews.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.emin.spaceflightnews.core.database.entity.FavoriteArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_articles ORDER BY savedAtEpochMillis DESC")
    fun observeAll(): Flow<List<FavoriteArticleEntity>>

    @Query("SELECT id FROM favorite_articles")
    fun observeIds(): Flow<List<Long>>

    @Query("SELECT * FROM favorite_articles WHERE id = :id")
    fun observeById(id: Long): Flow<FavoriteArticleEntity?>

    @Upsert
    suspend fun upsert(article: FavoriteArticleEntity)

    @Query("DELETE FROM favorite_articles WHERE id = :id")
    suspend fun deleteById(id: Long)
}
