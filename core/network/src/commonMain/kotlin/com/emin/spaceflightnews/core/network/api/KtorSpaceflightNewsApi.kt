package com.emin.spaceflightnews.core.network.api

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.common.takeIfNotBlank
import com.emin.spaceflightnews.core.network.dto.ArticleDto
import com.emin.spaceflightnews.core.network.dto.PagedResponseDto
import com.emin.spaceflightnews.core.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorSpaceflightNewsApi(private val client: HttpClient) : SpaceflightNewsApi {
    override suspend fun getArticles(
        limit: Int,
        offset: Int,
        search: String?,
        ordering: String,
    ): DataResult<PagedResponseDto<ArticleDto>> = safeApiCall {
        client.get("articles/") {
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("ordering", ordering)
            search.takeIfNotBlank()?.let { parameter("search", it) }
        }.body()
    }

    override suspend fun getArticle(id: Long): DataResult<ArticleDto> = safeApiCall {
        client.get("articles/$id/").body()
    }
}
