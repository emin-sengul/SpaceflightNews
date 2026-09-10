package com.emin.spaceflightnews.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emin.spaceflightnews.core.common.stateInWhileSubscribed
import com.emin.spaceflightnews.core.domain.usecase.ObserveFavoritesUseCase
import com.emin.spaceflightnews.core.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoritesViewModel(
    observeFavorites: ObserveFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    val state: StateFlow<FavoritesUiState> = observeFavorites()
        .map { articles -> articles.toFavoritesUiState() }
        .stateInWhileSubscribed(viewModelScope, FavoritesUiState())

    fun onIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.FavoriteRemoved -> viewModelScope.launch {
                toggleFavorite(intent.article)
            }
        }
    }
}
