package com.emin.spaceflightnews.core.domain.usecase

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.repository.ArticleRepository

class FetchArticleUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke(id: Long): DataResult<Article> = repository.fetchArticle(id)
}
