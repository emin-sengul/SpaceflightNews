@file:OptIn(ExperimentalTime::class)

package com.emin.spaceflightnews.core.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Article(
    val id: Long,
    val title: String,
    val summary: String,
    val imageUrl: String?,
    val newsSite: String,
    val url: String,
    val publishedAt: Instant,
    val updatedAt: Instant?,
    val authors: List<String>,
    val isFavorite: Boolean = false,
)
