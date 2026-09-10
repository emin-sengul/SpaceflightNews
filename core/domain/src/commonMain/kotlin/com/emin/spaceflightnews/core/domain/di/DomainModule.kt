package com.emin.spaceflightnews.core.domain.di

import com.emin.spaceflightnews.core.domain.usecase.FetchArticleUseCase
import com.emin.spaceflightnews.core.domain.usecase.LoadMoreFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.ObserveArticleUseCase
import com.emin.spaceflightnews.core.domain.usecase.ObserveFavoritesUseCase
import com.emin.spaceflightnews.core.domain.usecase.ObserveFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.RefreshFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.SearchArticlesUseCase
import com.emin.spaceflightnews.core.domain.usecase.ToggleFavoriteUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { ObserveFeedUseCase(get()) }
    factory { RefreshFeedUseCase(get()) }
    factory { LoadMoreFeedUseCase(get()) }
    factory { SearchArticlesUseCase(get()) }
    factory { ObserveArticleUseCase(get()) }
    factory { FetchArticleUseCase(get()) }
    factory { ObserveFavoritesUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
}
