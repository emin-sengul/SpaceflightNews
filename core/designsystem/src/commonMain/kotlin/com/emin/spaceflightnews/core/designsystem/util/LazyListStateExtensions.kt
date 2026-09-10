package com.emin.spaceflightnews.core.designsystem.util

import androidx.compose.foundation.lazy.LazyListState

suspend fun LazyListState.scrollToTop() {
    animateScrollToItem(FIRST_ITEM_INDEX)
}

private const val FIRST_ITEM_INDEX = 0
