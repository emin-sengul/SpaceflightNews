package com.emin.spaceflightnews.feature.articles.di

import com.emin.spaceflightnews.feature.articles.ArticlesViewModel
import org.koin.dsl.module

val articlesModule = module {
    factory {
        ArticlesViewModel(
            observeFeed = get(),
            refreshFeed = get(),
            loadMoreFeed = get(),
            searchArticles = get(),
            observeFavorites = get(),
            toggleFavorite = get(),
        )
    }
}
