package com.emin.spaceflightnews.core.database.entity

data class ArticleColumns(
    val title: String,
    val summary: String,
    val imageUrl: String?,
    val newsSite: String,
    val url: String,
    val publishedAtEpochMillis: Long,
    val updatedAtEpochMillis: Long?,
    val authors: List<String>,
)
