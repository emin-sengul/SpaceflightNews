package com.emin.spaceflightnews.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    actual fun createBuilder(): RoomDatabase.Builder<SpaceflightDatabase> =
        Room.databaseBuilder<SpaceflightDatabase>(
            name = documentDirectory() + "/" + DATABASE_NAME,
        )

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path) {
            "Could not resolve the iOS documents directory"
        }
    }
}
