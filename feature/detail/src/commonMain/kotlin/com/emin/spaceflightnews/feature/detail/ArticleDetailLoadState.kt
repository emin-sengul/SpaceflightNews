package com.emin.spaceflightnews.feature.detail

import com.emin.spaceflightnews.core.common.AppError

internal data class ArticleDetailLoadState(
    val isLoading: Boolean = true,
    val error: AppError? = null,
)
