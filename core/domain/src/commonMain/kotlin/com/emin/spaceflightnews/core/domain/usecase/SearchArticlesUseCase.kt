package com.emin.spaceflightnews.core.domain.usecase

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.domain.model.PagedArticles
import com.emin.spaceflightnews.core.domain.repository.ArticleRepository

class SearchArticlesUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke(
        query: String,
        offset: Int = 0,
        limit: Int = DEFAULT_PAGE_SIZE,
    ): DataResult<PagedArticles> = repository.searchArticles(query.trim(), offset, limit)

    companion object {
        const val DEFAULT_PAGE_SIZE: Int = 20
    }
}
