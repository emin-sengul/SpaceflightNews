package com.emin.spaceflightnews.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.emin.spaceflightnews.core.database.dao.ArticleDao
import com.emin.spaceflightnews.core.database.dao.FavoriteDao
import com.emin.spaceflightnews.core.database.entity.AuthorListConverter
import com.emin.spaceflightnews.core.database.entity.CachedArticleEntity
import com.emin.spaceflightnews.core.database.entity.FavoriteArticleEntity

@Database(
    entities = [CachedArticleEntity::class, FavoriteArticleEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(AuthorListConverter::class)
@ConstructedBy(SpaceflightDatabaseConstructor::class)
abstract class SpaceflightDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun favoriteDao(): FavoriteDao
}

@Suppress("KotlinNoActualForExpect")
expect object SpaceflightDatabaseConstructor : RoomDatabaseConstructor<SpaceflightDatabase> {
    override fun initialize(): SpaceflightDatabase
}

internal const val DATABASE_NAME: String = "spaceflight_news.db"
