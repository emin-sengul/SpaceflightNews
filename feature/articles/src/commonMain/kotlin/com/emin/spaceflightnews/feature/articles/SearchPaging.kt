package com.emin.spaceflightnews.feature.articles

internal class SearchPaging {

    var offset: Int = 0
        private set

    private var totalCount: Int = Int.MAX_VALUE

    val endReached: Boolean get() = offset >= totalCount

    fun reset() {
        offset = 0
        totalCount = Int.MAX_VALUE
    }

    fun advance(loadedCount: Int, totalCount: Int) {
        offset += loadedCount
        this.totalCount = totalCount
    }
}
