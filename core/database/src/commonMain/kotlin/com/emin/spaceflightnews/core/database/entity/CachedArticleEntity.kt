package com.emin.spaceflightnews.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_articles")
data class CachedArticleEntity(
    @PrimaryKey val id: Long,
    @Embedded val article: ArticleColumns,
)
