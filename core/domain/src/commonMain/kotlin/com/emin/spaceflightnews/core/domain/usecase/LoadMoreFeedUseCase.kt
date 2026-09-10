package com.emin.spaceflightnews.core.domain.usecase

import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.domain.repository.ArticleRepository

class LoadMoreFeedUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke(): DataResult<Boolean> = repository.loadMoreFeed()
}
