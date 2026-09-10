package com.emin.spaceflightnews.core.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class FeedPagingCursor {

    private val mutex = Mutex()
    private var loadedCount: Int = 0
    private var totalCount: Int = Int.MAX_VALUE

    suspend fun snapshot(): Snapshot = mutex.withLock {
        Snapshot(loadedCount = loadedCount, totalCount = totalCount)
    }

    suspend fun reset(loadedCount: Int, totalCount: Int) = mutex.withLock {
        this.loadedCount = loadedCount
        this.totalCount = totalCount
    }

    suspend fun advance(offset: Int, loadedCount: Int, totalCount: Int): Boolean = mutex.withLock {
        this.loadedCount = offset + loadedCount
        this.totalCount = totalCount
        loadedCount == 0 || this.loadedCount >= totalCount
    }

    data class Snapshot(val loadedCount: Int, val totalCount: Int) {
        val endReached: Boolean get() = loadedCount >= totalCount
    }
}
