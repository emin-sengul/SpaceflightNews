package com.emin.spaceflightnews.di

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.emin.spaceflightnews.core.data.di.dataModule
import com.emin.spaceflightnews.feature.articles.di.articlesModule
import com.emin.spaceflightnews.feature.detail.di.detailModule
import com.emin.spaceflightnews.feature.favorites.di.favoritesModule
import io.ktor.client.HttpClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    enableNetworkLogging: Boolean = true,
    config: KoinAppDeclaration? = null,
): KoinApplication {
    val koinApplication = startKoin {
        config?.invoke(this)
        modules(
            dataModule(enableNetworkLogging),
            articlesModule,
            detailModule,
            favoritesModule,
        )
    }

    setUpImageLoader(koinApplication.koin.get())
    return koinApplication
}

private fun setUpImageLoader(httpClient: HttpClient) {
    SingletonImageLoader.setSafe { context: PlatformContext ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory(httpClient = { httpClient })) }
            .crossfade(true)
            .build()
    }
}
