package com.emin.spaceflightnews.core.database.di

import com.emin.spaceflightnews.core.database.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDatabaseModule: Module = module {
    single { DatabaseFactory() }
}
