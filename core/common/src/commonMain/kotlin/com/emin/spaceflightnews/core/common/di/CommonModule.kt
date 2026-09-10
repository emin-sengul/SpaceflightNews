package com.emin.spaceflightnews.core.common.di

import com.emin.spaceflightnews.core.common.DefaultDispatcherProvider
import com.emin.spaceflightnews.core.common.DispatcherProvider
import org.koin.dsl.module

val commonModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}
