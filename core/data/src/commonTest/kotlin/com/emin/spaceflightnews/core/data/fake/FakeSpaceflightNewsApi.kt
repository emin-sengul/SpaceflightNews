package com.emin.spaceflightnews.core.data.fake

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.network.api.SpaceflightNewsApi
import com.emin.spaceflightnews.core.network.dto.ArticleDto
import com.emin.spaceflightnews.core.network.dto.AuthorDto
import com.emin.spaceflightnews.core.network.dto.PagedResponseDto

internal class FakeSpaceflightNewsApi : SpaceflightNewsApi {
    data class Call(val limit: Int, val offset: Int, val search: String?)

    private val queuedPages = ArrayDeque<DataResult<PagedResponseDto<ArticleDto>>>()
    val calls = mutableListOf<Call>()

    var articleResponse: DataResult<ArticleDto> = DataResult.Success(articleDto(id = 1))

    fun enqueue(vararg responses: DataResult<PagedResponseDto<ArticleDto>>) {
        queuedPages.addAll(responses)
    }

    fun enqueuePage(articles: List<ArticleDto>, totalCount: Int = articles.size) {
        queuedPages.addLast(
            DataResult.Success(PagedResponseDto(count = totalCount, results = articles)),
        )
    }

    override suspend fun getArticles(
        limit: Int,
        offset: Int,
        search: String?,
        ordering: String,
    ): DataResult<PagedResponseDto<ArticleDto>> {
        calls += Call(limit = limit, offset = offset, search = search)
        return queuedPages.removeFirstOrNull()
            ?: DataResult.Success(PagedResponseDto(count = 0, results = emptyList()))
    }

    override suspend fun getArticle(id: Long): DataResult<ArticleDto> = articleResponse
}

internal fun articleDto(
    id: Long,
    title: String = "Article $id",
    publishedAt: String = "2026-09-0${(id % 9) + 1}T10:00:00Z",
    newsSite: String = "Space Scout",
    imageUrl: String? = "https://example.com/$id.png",
): ArticleDto = ArticleDto(
    id = id,
    title = title,
    authors = listOf(AuthorDto(name = "Reporter $id")),
    url = "https://example.com/articles/$id",
    imageUrl = imageUrl,
    newsSite = newsSite,
    summary = "Summary of article $id",
    publishedAt = publishedAt,
    updatedAt = null,
    featured = false,
)
