@file:OptIn(ExperimentalTime::class)

package com.emin.spaceflightnews.core.data.mapper

import com.emin.spaceflightnews.core.common.takeIfNotBlank
import com.emin.spaceflightnews.core.database.entity.ArticleColumns
import com.emin.spaceflightnews.core.database.entity.CachedArticleEntity
import com.emin.spaceflightnews.core.database.entity.FavoriteArticleEntity
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.model.PagedArticles
import com.emin.spaceflightnews.core.network.dto.ArticleDto
import com.emin.spaceflightnews.core.network.dto.PagedResponseDto
import kotlin.time.ExperimentalTime

internal fun ArticleDto.toDomain(isFavorite: Boolean = false): Article = Article(
    id = id,
    title = title,
    summary = summary,
    imageUrl = imageUrl,
    newsSite = newsSite,
    url = url,
    publishedAt = publishedAt.toInstantOrEpoch(),
    updatedAt = updatedAt?.toInstantOrNull(),
    authors = authorNames(),
    isFavorite = isFavorite,
)

internal fun ArticleDto.toCachedEntity(): CachedArticleEntity =
    CachedArticleEntity(id = id, article = toColumns())

internal fun CachedArticleEntity.toDomain(isFavorite: Boolean): Article =
    article.toDomain(id = id, isFavorite = isFavorite)

internal fun FavoriteArticleEntity.toDomain(): Article =
    article.toDomain(id = id, isFavorite = true)

internal fun Article.toFavoriteEntity(savedAtEpochMillis: Long): FavoriteArticleEntity =
    FavoriteArticleEntity(
        id = id,
        article = toColumns(),
        savedAtEpochMillis = savedAtEpochMillis,
    )

internal fun List<CachedArticleEntity>.toArticles(favoriteIds: Set<Long>): List<Article> =
    map { entity -> entity.toDomain(isFavorite = entity.id in favoriteIds) }

internal fun List<FavoriteArticleEntity>.toArticles(): List<Article> = map { it.toDomain() }

internal fun PagedResponseDto<ArticleDto>.toCachedEntities(): List<CachedArticleEntity> =
    results.map { it.toCachedEntity() }

internal fun PagedResponseDto<ArticleDto>.toPagedArticles(favoriteIds: Set<Long>): PagedArticles =
    PagedArticles(
        articles = results.map { dto -> dto.toDomain(isFavorite = dto.id in favoriteIds) },
        totalCount = count,
    )

private fun ArticleDto.toColumns(): ArticleColumns = ArticleColumns(
    title = title,
    summary = summary,
    imageUrl = imageUrl,
    newsSite = newsSite,
    url = url,
    publishedAtEpochMillis = publishedAt.toInstantOrEpoch().toEpochMillis(),
    updatedAtEpochMillis = updatedAt?.toInstantOrNull()?.toEpochMillis(),
    authors = authorNames(),
)

private fun ArticleColumns.toDomain(id: Long, isFavorite: Boolean): Article = Article(
    id = id,
    title = title,
    summary = summary,
    imageUrl = imageUrl,
    newsSite = newsSite,
    url = url,
    publishedAt = publishedAtEpochMillis.toInstant(),
    updatedAt = updatedAtEpochMillis?.toInstant(),
    authors = authors,
    isFavorite = isFavorite,
)

private fun Article.toColumns(): ArticleColumns = ArticleColumns(
    title = title,
    summary = summary,
    imageUrl = imageUrl,
    newsSite = newsSite,
    url = url,
    publishedAtEpochMillis = publishedAt.toEpochMillis(),
    updatedAtEpochMillis = updatedAt?.toEpochMillis(),
    authors = authors,
)

private fun ArticleDto.authorNames(): List<String> =
    authors.mapNotNull { it.name.takeIfNotBlank() }
