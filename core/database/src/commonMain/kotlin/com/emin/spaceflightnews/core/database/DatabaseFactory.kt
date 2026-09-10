package com.emin.spaceflightnews.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.emin.spaceflightnews.core.common.ioDispatcher

expect class DatabaseFactory {
    fun createBuilder(): RoomDatabase.Builder<SpaceflightDatabase>
}

fun createSpaceflightDatabase(factory: DatabaseFactory): SpaceflightDatabase =
    factory.createBuilder()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(ioDispatcher)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
