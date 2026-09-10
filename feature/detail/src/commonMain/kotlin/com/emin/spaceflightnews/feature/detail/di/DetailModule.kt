package com.emin.spaceflightnews.feature.detail.di

import com.emin.spaceflightnews.feature.detail.ArticleDetailViewModel
import com.emin.spaceflightnews.feature.detail.platformDetailModule
import org.koin.dsl.module

val detailModule = module {
    includes(platformDetailModule)

    factory { parameters ->
        ArticleDetailViewModel(
            articleId = parameters.get(),
            observeArticle = get(),
            fetchArticle = get(),
            toggleFavorite = get(),
            linkHandler = get(),
        )
    }
}
