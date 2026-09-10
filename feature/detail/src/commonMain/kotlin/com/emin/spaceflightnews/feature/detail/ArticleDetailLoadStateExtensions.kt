package com.emin.spaceflightnews.feature.detail

import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.common.isNull
import com.emin.spaceflightnews.core.domain.model.Article

internal fun ArticleDetailLoadState.loading(): ArticleDetailLoadState =
    copy(isLoading = true, error = null)

internal fun ArticleDetailLoadState.loaded(): ArticleDetailLoadState =
    copy(isLoading = false, error = null)

internal fun ArticleDetailLoadState.failed(error: AppError): ArticleDetailLoadState =
    copy(isLoading = false, error = error)

internal fun ArticleDetailLoadState.toUiState(article: Article?): ArticleDetailUiState =
    ArticleDetailUiState(
        article = article,
        isLoading = article.isNull() && isLoading,
        error = error.takeIf { article.isNull() },
    )
