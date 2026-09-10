package com.emin.spaceflightnews.core.network.di

import com.emin.spaceflightnews.core.network.api.KtorSpaceflightNewsApi
import com.emin.spaceflightnews.core.network.api.SpaceflightNewsApi
import com.emin.spaceflightnews.core.network.createHttpClient
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

fun networkModule(enableLogging: Boolean): Module = module {
    single<HttpClient> { createHttpClient(enableLogging = enableLogging) }
    single<SpaceflightNewsApi> { KtorSpaceflightNewsApi(get()) }
}
