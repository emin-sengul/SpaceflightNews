package com.emin.spaceflightnews.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_articles")
data class FavoriteArticleEntity(
    @PrimaryKey val id: Long,
    @Embedded val article: ArticleColumns,
    val savedAtEpochMillis: Long,
)
