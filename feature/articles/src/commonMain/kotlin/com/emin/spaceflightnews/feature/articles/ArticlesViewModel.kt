@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.emin.spaceflightnews.feature.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emin.spaceflightnews.core.common.DataResult
import com.emin.spaceflightnews.core.common.isNull
import com.emin.spaceflightnews.core.common.onFailure
import com.emin.spaceflightnews.core.common.onSuccess
import com.emin.spaceflightnews.core.common.stateInWhileSubscribed
import com.emin.spaceflightnews.core.common.takeIfNotBlank
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.model.favoriteIds
import com.emin.spaceflightnews.core.domain.model.withFavoriteIds
import com.emin.spaceflightnews.core.domain.usecase.LoadMoreFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.ObserveFavoritesUseCase
import com.emin.spaceflightnews.core.domain.usecase.ObserveFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.RefreshFeedUseCase
import com.emin.spaceflightnews.core.domain.usecase.SearchArticlesUseCase
import com.emin.spaceflightnews.core.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArticlesViewModel(
    private val observeFeed: ObserveFeedUseCase,
    private val refreshFeed: RefreshFeedUseCase,
    private val loadMoreFeed: LoadMoreFeedUseCase,
    private val searchArticles: SearchArticlesUseCase,
    private val observeFavorites: ObserveFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val loadState = MutableStateFlow(FeedLoadState())
    private val searchResults = MutableStateFlow<List<Article>>(emptyList())
    private val searchPaging = SearchPaging()

    private val articles: Flow<List<Article>> = query
        .debounce(SEARCH_DEBOUNCE_MILLIS)
        .distinctUntilChanged()
        .flatMapLatest { currentQuery ->
            currentQuery.takeIfNotBlank()?.let(::searchFeed) ?: cachedFeed()
        }

    val state: StateFlow<ArticlesUiState> =
        combine(query, articles, loadState) { currentQuery, articles, load ->
            load.toUiState(query = currentQuery, articles = articles)
        }.stateInWhileSubscribed(viewModelScope, ArticlesUiState())

    fun onIntent(intent: ArticlesIntent) {
        when (intent) {
            is ArticlesIntent.QueryChanged -> query.value = intent.query
            ArticlesIntent.Refresh -> refresh()
            ArticlesIntent.Retry -> refresh()
            ArticlesIntent.LoadMore -> loadMore()
            is ArticlesIntent.FavoriteToggled -> viewModelScope.launch {
                toggleFavorite(intent.article)
            }
            ArticlesIntent.BannerDismissed -> loadState.update(FeedLoadState::clearError)
        }
    }

    private fun cachedFeed(): Flow<List<Article>> = flow {
        searchResults.value = emptyList()
        loadState.update(FeedLoadState::restartPaging)
        loadFeedIfCacheIsEmpty()
        emitAll(observeFeed())
    }

    private fun searchFeed(searchQuery: String): Flow<List<Article>> = flow {
        runSearch(searchQuery)
        emitAll(
            combine(searchResults, observeFavorites()) { results, favorites ->
                results.withFavoriteIds(favorites.favoriteIds())
            },
        )
    }

    private suspend fun loadFeedIfCacheIsEmpty() {
        if (observeFeed().first().isNotEmpty()) {
            loadState.update(FeedLoadState::loaded)
            return
        }
        loadState.update(FeedLoadState::loading)
        applyFeedResult(refreshFeed())
    }

    private suspend fun runSearch(searchQuery: String) {
        searchPaging.reset()
        searchResults.value = emptyList()
        loadState.update(FeedLoadState::startingSearch)

        searchArticles(searchQuery, offset = 0, limit = PAGE_SIZE)
            .onSuccess { page ->
                searchResults.value = page.articles
                searchPaging.advance(page.articles.size, page.totalCount)
                loadState.update { it.pageLoaded(searchPaging.endReached).loaded() }
            }
            .onFailure { failure -> loadState.update { it.failed(failure) } }
    }

    private fun refresh() {
        val searchTerm = query.value.takeIfNotBlank()
        viewModelScope.launch {
            loadState.update(FeedLoadState::refreshing)
            if (searchTerm.isNull()) {
                val result = refreshFeed()
                loadState.update(FeedLoadState::restartPaging)
                applyFeedResult(result)
            } else {
                runSearch(searchTerm)
            }
            loadState.update(FeedLoadState::refreshed)
        }
    }

    private fun loadMore() {
        val current = loadState.value
        if (current.isLoadingMore || current.endReached || current.isLoading) return

        viewModelScope.launch {
            loadState.update(FeedLoadState::loadingMore)
            val searchTerm = query.value.takeIfNotBlank()
            if (searchTerm.isNull()) loadNextFeedPage() else loadNextSearchPage(searchTerm)
            loadState.update(FeedLoadState::loadedMore)
        }
    }

    private suspend fun loadNextFeedPage() {
        loadMoreFeed()
            .onSuccess { endReached -> loadState.update { it.pageLoaded(endReached) } }
            .onFailure { failure -> loadState.update { it.failed(failure) } }
    }

    private suspend fun loadNextSearchPage(searchQuery: String) {
        searchArticles(searchQuery, offset = searchPaging.offset, limit = PAGE_SIZE)
            .onSuccess { page ->
                searchResults.update { current -> current + page.articles }
                searchPaging.advance(page.articles.size, page.totalCount)
                val endReached = page.articles.isEmpty() || searchPaging.endReached
                loadState.update { it.pageLoaded(endReached) }
            }
            .onFailure { failure -> loadState.update { it.failed(failure) } }
    }

    private fun applyFeedResult(result: DataResult<Unit>) {
        result
            .onSuccess { loadState.update(FeedLoadState::loaded) }
            .onFailure { failure -> loadState.update { it.failed(failure) } }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 350L
        const val PAGE_SIZE = 20
    }
}
