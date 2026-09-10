package com.emin.spaceflightnews.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val id: Long,
    val title: String = "",
    val authors: List<AuthorDto> = emptyList(),
    val url: String = "",
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("news_site") val newsSite: String = "",
    val summary: String = "",
    @SerialName("published_at") val publishedAt: String,
    @SerialName("updated_at") val updatedAt: String? = null,
    val featured: Boolean = false,
)
