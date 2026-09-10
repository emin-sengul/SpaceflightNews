package com.emin.spaceflightnews.core.data.di

import com.emin.spaceflightnews.core.common.di.commonModule
import com.emin.spaceflightnews.core.data.repository.OfflineFirstArticleRepository
import com.emin.spaceflightnews.core.database.di.databaseModule
import com.emin.spaceflightnews.core.domain.di.domainModule
import com.emin.spaceflightnews.core.domain.repository.ArticleRepository
import com.emin.spaceflightnews.core.network.di.networkModule
import org.koin.core.module.Module
import org.koin.dsl.module

fun dataModule(enableNetworkLogging: Boolean): Module = module {
    includes(
        commonModule,
        networkModule(enableNetworkLogging),
        databaseModule,
        domainModule,
    )

    single<ArticleRepository> {
        OfflineFirstArticleRepository(
            api = get(),
            articleDao = get(),
            favoriteDao = get(),
            dispatchers = get(),
        )
    }
}
