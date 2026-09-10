package com.emin.spaceflightnews.core.domain.model

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PagedArticlesTest {
    private val page = PagedArticles(articles = emptyList(), totalCount = 40)

    @Test
    fun theEndIsNotReachedWhileFewerArticlesAreLoadedThanExist() {
        assertFalse(page.endReached(loadedCount = 20))
    }

    @Test
    fun theEndIsReachedOnTheExactCount() {
        assertTrue(page.endReached(loadedCount = 40))
    }

    @Test
    fun theEndIsReachedIfMoreArrivedThanReported() {
        assertTrue(page.endReached(loadedCount = 41))
    }
}
