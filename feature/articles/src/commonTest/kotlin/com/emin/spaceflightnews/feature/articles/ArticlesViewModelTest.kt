package com.emin.spaceflightnews.feature.articles

import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.domain.model.PagedArticles
import com.emin.spaceflightnews.core.domain.usecase.LoadMoreFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.ObserveFavoritesUseCase
import com.emin.spaceflightnews.core.domain.usecase.ObserveFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.RefreshFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.SearchArticlesUseCase
import com.emin.spaceflightnews.core.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesViewModelTest {
    private val repository = FakeArticleRepository()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = ArticlesViewModel(
        observeFeed = ObserveFeedUseCase(repository),
        refreshFeed = RefreshFeedUseCase(repository),
        loadMoreFeed = LoadMoreFeedUseCase(repository),
        searchArticles = SearchArticlesUseCase(repository),
        observeFavorites = ObserveFavoritesUseCase(repository),
        toggleFavorite = ToggleFavoriteUseCase(repository),
    )

    @Test
    fun refreshesOnceWhenTheCacheIsEmpty() = runTest(testDispatcher) {
        repository.refreshBehaviour = {
            repository.feed.value = listOf(testArticle(1), testArticle(2))
            DataResult.Success(Unit)
        }
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }

        advanceUntilIdle()

        assertEquals(1, repository.refreshCount)
        assertEquals(listOf(1L, 2L), viewModel.state.value.articles.map { it.id })
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun doesNotHitTheNetworkWhenTheCacheAlreadyHasArticles() = runTest(testDispatcher) {
        repository.feed.value = listOf(testArticle(1))
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }

        advanceUntilIdle()

        assertEquals(0, repository.refreshCount)
        assertEquals(listOf(1L), viewModel.state.value.articles.map { it.id })
    }

    @Test
    fun aQuerySwitchesToSearchResultsOnlyAfterTheDebounce() = runTest(testDispatcher) {
        repository.feed.value = listOf(testArticle(1))
        repository.searchResult = DataResult.Success(
            PagedArticles(articles = listOf(testArticle(99, title = "Mars sample return")), totalCount = 1),
        )
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onIntent(ArticlesIntent.QueryChanged("mars"))
        advanceTimeBy(200)
        runCurrent()

        assertNull(repository.lastSearchQuery)
        assertEquals(listOf(1L), viewModel.state.value.articles.map { it.id })

        advanceTimeBy(300)
        advanceUntilIdle()

        assertEquals("mars", repository.lastSearchQuery)
        assertEquals(listOf(99L), viewModel.state.value.articles.map { it.id })
        assertTrue(viewModel.state.value.isSearchActive)
    }

    @Test
    fun clearingTheQueryReturnsToTheCachedFeed() = runTest(testDispatcher) {
        repository.feed.value = listOf(testArticle(1))
        repository.searchResult = DataResult.Success(
            PagedArticles(articles = listOf(testArticle(99)), totalCount = 1),
        )
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onIntent(ArticlesIntent.QueryChanged("mars"))
        advanceUntilIdle()
        assertEquals(listOf(99L), viewModel.state.value.articles.map { it.id })

        viewModel.onIntent(ArticlesIntent.QueryChanged(""))
        advanceUntilIdle()

        assertEquals(listOf(1L), viewModel.state.value.articles.map { it.id })
        assertFalse(viewModel.state.value.isSearchActive)
    }

    @Test
    fun aFailureWithNothingToShowIsBlocking() = runTest(testDispatcher) {
        repository.refreshBehaviour = { DataResult.Failure(AppError.NoConnection) }
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }

        advanceUntilIdle()

        assertEquals(AppError.NoConnection, viewModel.state.value.blockingError)
        assertNull(viewModel.state.value.bannerError)
    }

    @Test
    fun aFailureOnTopOfCachedArticlesIsOnlyABanner() = runTest(testDispatcher) {
        repository.feed.value = listOf(testArticle(1))
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        repository.refreshBehaviour = { DataResult.Failure(AppError.Timeout) }
        viewModel.onIntent(ArticlesIntent.Refresh)
        advanceUntilIdle()

        assertEquals(AppError.Timeout, viewModel.state.value.bannerError)
        assertNull(viewModel.state.value.blockingError)

        assertEquals(listOf(1L), viewModel.state.value.articles.map { it.id })
    }

    @Test
    fun dismissingTheBannerClearsIt() = runTest(testDispatcher) {
        repository.feed.value = listOf(testArticle(1))
        repository.refreshBehaviour = { DataResult.Failure(AppError.Timeout) }
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onIntent(ArticlesIntent.Refresh)
        advanceUntilIdle()
        viewModel.onIntent(ArticlesIntent.BannerDismissed)
        advanceUntilIdle()

        assertNull(viewModel.state.value.bannerError)
    }

    @Test
    fun loadMoreStopsOnceTheEndOfTheFeedIsReported() = runTest(testDispatcher) {
        repository.feed.value = listOf(testArticle(1))
        repository.loadMoreResult = DataResult.Success(true)
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onIntent(ArticlesIntent.LoadMore)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.canLoadMore)

        viewModel.onIntent(ArticlesIntent.LoadMore)
        advanceUntilIdle()

        assertEquals(1, repository.loadMoreCount)
    }

    @Test
    fun togglingAFavoriteIsDelegatedToTheRepository() = runTest(testDispatcher) {
        val article = testArticle(1, isFavorite = false)
        repository.feed.value = listOf(article)
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.state.collect() }
        advanceUntilIdle()

        viewModel.onIntent(ArticlesIntent.FavoriteToggled(article))
        advanceUntilIdle()

        assertEquals(listOf(1L to true), repository.favoriteChanges)
    }
}
