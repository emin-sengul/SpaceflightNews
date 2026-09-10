package com.emin.spaceflightnews.feature.articles.effect

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
internal fun InfiniteScrollEffect(
    listState: LazyListState,
    itemCount: Int,
    enabled: Boolean,
    onLoadMore: () -> Unit,
) {
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    LaunchedEffect(listState, itemCount, enabled) {
        if (!enabled || itemCount == 0) return@LaunchedEffect

        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .distinctUntilChanged()
            .filter { lastVisibleIndex -> lastVisibleIndex >= itemCount - LOAD_MORE_THRESHOLD }
            .collect { currentOnLoadMore() }
    }
}

private const val LOAD_MORE_THRESHOLD = 4
