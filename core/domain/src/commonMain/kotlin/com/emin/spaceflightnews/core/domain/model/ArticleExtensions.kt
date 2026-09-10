package com.emin.spaceflightnews.core.domain.model

import com.emin.spaceflightnews.core.common.appendIfNotBlank
import com.emin.spaceflightnews.core.common.joinNotBlank
import com.emin.spaceflightnews.core.common.takeIfHttpUrl

val Article.sourceUrl: String? get() = url.takeIfHttpUrl()

fun Article.authorsLabel(): String = authors.joinNotBlank()

fun Article.shareText(): String = title.appendIfNotBlank(sourceUrl.orEmpty(), SHARE_SEPARATOR)

fun Iterable<Article>.favoriteIds(): Set<Long> = mapTo(mutableSetOf()) { it.id }

fun List<Article>.withFavoriteIds(favoriteIds: Set<Long>): List<Article> =
    map { article -> article.copy(isFavorite = article.id in favoriteIds) }

private const val SHARE_SEPARATOR = "\n\n"
