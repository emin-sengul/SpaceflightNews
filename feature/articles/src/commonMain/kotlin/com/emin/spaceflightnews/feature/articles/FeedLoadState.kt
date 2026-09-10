package com.emin.spaceflightnews.feature.articles

import com.emin.spaceflightnews.core.common.AppError

internal data class FeedLoadState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false,
    val error: AppError? = null,
)
