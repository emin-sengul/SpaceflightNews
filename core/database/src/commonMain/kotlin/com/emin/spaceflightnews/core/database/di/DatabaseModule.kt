package com.emin.spaceflightnews.core.database.di

import com.emin.spaceflightnews.core.database.SpaceflightDatabase
import com.emin.spaceflightnews.core.database.createSpaceflightDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDatabaseModule: Module

val databaseModule = module {
    includes(platformDatabaseModule)

    single { createSpaceflightDatabase(get()) }
    single { get<SpaceflightDatabase>().articleDao() }
    single { get<SpaceflightDatabase>().favoriteDao() }
}
