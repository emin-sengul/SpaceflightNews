package com.emin.spaceflightnews.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(private val context: Context) {
    actual fun createBuilder(): RoomDatabase.Builder<SpaceflightDatabase> {
        val databaseFile = context.getDatabasePath(DATABASE_NAME)
        return Room.databaseBuilder<SpaceflightDatabase>(
            context = context.applicationContext,
            name = databaseFile.absolutePath,
        )
    }
}
