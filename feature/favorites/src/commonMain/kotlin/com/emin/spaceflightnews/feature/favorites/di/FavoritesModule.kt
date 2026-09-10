package com.emin.spaceflightnews.feature.favorites.di

import com.emin.spaceflightnews.feature.favorites.FavoritesViewModel
import org.koin.dsl.module

val favoritesModule = module {
    factory {
        FavoritesViewModel(
            observeFavorites = get(),
            toggleFavorite = get(),
        )
    }
}
