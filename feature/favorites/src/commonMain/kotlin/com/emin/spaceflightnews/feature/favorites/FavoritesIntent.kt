package com.emin.spaceflightnews.feature.favorites

import com.emin.spaceflightnews.core.domain.model.Article

sealed interface FavoritesIntent {
    data class FavoriteRemoved(val article: Article) : FavoritesIntent
}
