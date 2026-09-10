package com.emin.spaceflightnews.feature.articles

import androidx.compose.ui.graphics.vector.ImageVector
import com.emin.spaceflightnews.core.designsystem.icon.SpaceflightIcons

internal fun ArticlesUiState.emptyTitle(): String =
    if (isSearchActive) "No matching articles" else "Nothing to read yet"

internal fun ArticlesUiState.emptyMessage(): String = if (isSearchActive) {
    "Try another keyword, for example a mission name or a launch provider."
} else {
    "Pull down to load the latest spaceflight news."
}

internal fun ArticlesUiState.emptyIcon(): ImageVector =
    if (isSearchActive) SpaceflightIcons.Search else SpaceflightIcons.Feed

internal val ArticlesUiState.showsAttribution: Boolean get() = !canLoadMore

internal val ArticlesUiState.canPageFurther: Boolean
    get() = canLoadMore && !isLoadingMore && !isLoading
