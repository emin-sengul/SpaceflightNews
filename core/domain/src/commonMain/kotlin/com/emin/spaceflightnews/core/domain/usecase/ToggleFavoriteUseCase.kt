package com.emin.spaceflightnews.core.domain.usecase

import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.repository.ArticleRepository

class ToggleFavoriteUseCase(private val repository: ArticleRepository) {
    suspend operator fun invoke(article: Article) {
        repository.setFavorite(article, isFavorite = !article.isFavorite)
    }
}
