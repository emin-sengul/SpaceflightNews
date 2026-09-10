package com.emin.spaceflightnews.core.data.repository

import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.common.DispatcherProvider
import com.emin.spaceflightnews.core.common.fold
import com.emin.spaceflightnews.core.data.fake.FakeArticleDao
import com.emin.spaceflightnews.core.data.fake.FakeFavoriteDao
import com.emin.spaceflightnews.core.data.fake.FakeSpaceflightNewsApi
import com.emin.spaceflightnews.core.data.fake.articleDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class OfflineFirstArticleRepositoryTest {
    private val api = FakeSpaceflightNewsApi()
    private val articleDao = FakeArticleDao()
    private val favoriteDao = FakeFavoriteDao()

    private fun repository(dispatcher: CoroutineDispatcher) = OfflineFirstArticleRepository(
        api = api,
        articleDao = articleDao,
        favoriteDao = favoriteDao,
        dispatchers = object : DispatcherProvider {
            override val io: CoroutineDispatcher = dispatcher
            override val default: CoroutineDispatcher = dispatcher
        },
    )

    @Test
    fun refreshReplacesTheCachedFeed() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))
        articleDao.upsertAll(listOf())
        api.enqueuePage(listOf(articleDto(1), articleDto(2)), totalCount = 2)

        repository.refreshFeed().orFail()

        assertEquals(setOf(1L, 2L), articleDao.current.map { it.id }.toSet())

        assertEquals(0, api.calls.single().offset)
    }

    @Test
    fun refreshDropsArticlesThatAreNoLongerInTheFeed() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))
        api.enqueuePage(listOf(articleDto(1), articleDto(2)), totalCount = 2)
        repository.refreshFeed().orFail()

        api.enqueuePage(listOf(articleDto(3)), totalCount = 1)
        repository.refreshFeed().orFail()

        assertEquals(listOf(3L), articleDao.current.map { it.id })
    }

    @Test
    fun theFeedIsDecoratedWithTheFavoriteState() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))
        api.enqueuePage(listOf(articleDto(1), articleDto(2)), totalCount = 2)
        repository.refreshFeed().orFail()

        val article = repository.observeFeed().first().first { it.id == 1L }
        repository.setFavorite(article, isFavorite = true)

        val feed = repository.observeFeed().first()
        assertTrue(feed.first { it.id == 1L }.isFavorite)
        assertTrue(!feed.first { it.id == 2L }.isFavorite)
    }

    @Test
    fun loadingMoreAppendsTheNextPageAndReportsTheEnd() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))
        api.enqueuePage(listOf(articleDto(1), articleDto(2)), totalCount = 3)
        repository.refreshFeed().orFail()

        api.enqueuePage(listOf(articleDto(3)), totalCount = 3)
        val endReached = repository.loadMoreFeed().orFail()

        assertEquals(setOf(1L, 2L, 3L), articleDao.current.map { it.id }.toSet())
        assertTrue(endReached, "Three of three articles are loaded, so the feed has ended")
        assertEquals(2, api.calls.last().offset)
    }

    @Test
    fun searchResultsAreNotWrittenIntoTheFeedCache() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))
        api.enqueuePage(listOf(articleDto(1)), totalCount = 1)
        repository.refreshFeed().orFail()

        api.enqueuePage(listOf(articleDto(99, title = "Mars sample return")), totalCount = 1)
        val page = repository.searchArticles(query = "mars", offset = 0, limit = 20).orFail()

        assertEquals(listOf(99L), page.articles.map { it.id })

        assertEquals(listOf(1L), articleDao.current.map { it.id })
        assertEquals("mars", api.calls.last().search)
    }

    @Test
    fun favoritingStoresAFullSnapshotSoItSurvivesACacheWipe() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))
        api.enqueuePage(listOf(articleDto(1, title = "Starship flight 12")), totalCount = 1)
        repository.refreshFeed().orFail()
        val article = repository.observeFeed().first().single()

        repository.setFavorite(article, isFavorite = true)
        articleDao.clear()

        val saved = repository.observeFavorites().first().single()
        assertEquals("Starship flight 12", saved.title)
        assertTrue(saved.isFavorite)

        assertEquals(1L, repository.observeArticle(1L).first()?.id)
    }

    @Test
    fun unfavoritingRemovesTheSnapshot() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))
        api.enqueuePage(listOf(articleDto(1)), totalCount = 1)
        repository.refreshFeed().orFail()
        val article = repository.observeFeed().first().single()

        repository.setFavorite(article, isFavorite = true)
        repository.setFavorite(article, isFavorite = false)

        assertTrue(repository.observeFavorites().first().isEmpty())
        assertTrue(favoriteDao.current.isEmpty())
    }

    @Test
    fun aFailedRefreshReportsTheErrorAndLeavesTheCacheAlone() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))
        api.enqueuePage(listOf(articleDto(1)), totalCount = 1)
        repository.refreshFeed().orFail()

        api.enqueue(DataResult.Failure(AppError.NoConnection))
        val result = repository.refreshFeed()

        assertEquals(AppError.NoConnection, assertIs<DataResult.Failure>(result).error)

        assertEquals(listOf(1L), articleDao.current.map { it.id })
    }

    @Test
    fun anUnknownArticleIsNullUntilItIsFetched() = runTest {
        val repository = repository(UnconfinedTestDispatcher(testScheduler))

        assertNull(repository.observeArticle(42L).first())

        api.articleResponse = DataResult.Success(articleDto(42, title = "Europa Clipper"))
        val fetched = repository.fetchArticle(42L).orFail()

        assertEquals("Europa Clipper", fetched.title)
        assertEquals("Europa Clipper", repository.observeArticle(42L).first()?.title)
    }

    private fun <T> DataResult<T>.orFail(): T = fold(
        onSuccess = { it },
        onFailure = { error -> fail("Expected success but failed with $error") },
    )
}
