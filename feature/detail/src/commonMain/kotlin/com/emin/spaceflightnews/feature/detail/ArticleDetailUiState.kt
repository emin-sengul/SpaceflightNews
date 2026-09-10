package com.emin.spaceflightnews.feature.detail

import androidx.compose.runtime.Immutable
import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.domain.model.Article

@Immutable
data class ArticleDetailUiState(
    val article: Article? = null,
    val isLoading: Boolean = true,
    val error: AppError? = null,
)
