package com.emin.spaceflightnews.core.network.api

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.network.dto.ArticleDto
import com.emin.spaceflightnews.core.network.dto.PagedResponseDto

interface SpaceflightNewsApi {
    suspend fun getArticles(
        limit: Int,
        offset: Int,
        search: String? = null,
        ordering: String = ORDERING_NEWEST_FIRST,
    ): DataResult<PagedResponseDto<ArticleDto>>

    suspend fun getArticle(id: Long): DataResult<ArticleDto>

    companion object {
        const val BASE_URL: String = "https://api.spaceflightnewsapi.net/v4/"
        const val ORDERING_NEWEST_FIRST: String = "-published_at"
    }
}
