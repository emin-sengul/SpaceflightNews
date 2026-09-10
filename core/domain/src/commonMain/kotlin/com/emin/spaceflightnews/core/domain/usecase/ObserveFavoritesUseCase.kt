package com.emin.spaceflightnews.core.domain.usecase

import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoritesUseCase(private val repository: ArticleRepository) {
    operator fun invoke(): Flow<List<Article>> = repository.observeFavorites()
}
