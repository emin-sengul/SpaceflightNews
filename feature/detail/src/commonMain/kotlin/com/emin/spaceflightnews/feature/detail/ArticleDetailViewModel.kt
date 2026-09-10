package com.emin.spaceflightnews.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.common.onFailure
import com.emin.spaceflightnews.core.common.onSuccess
import com.emin.spaceflightnews.core.common.stateInWhileSubscribed
import com.emin.spaceflightnews.core.domain.model.shareText
import com.emin.spaceflightnews.core.domain.model.sourceUrl
import com.emin.spaceflightnews.core.domain.usecase.FetchArticleUseCase
import com.emin.spaceflightnews.core.domain.usecase.ObserveArticleUseCase
import com.emin.spaceflightnews.core.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val articleId: Long,
    private val observeArticle: ObserveArticleUseCase,
    private val fetchArticle: FetchArticleUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val linkHandler: ExternalLinkHandler,
) : ViewModel() {

    private val loadState = MutableStateFlow(ArticleDetailLoadState())

    val state: StateFlow<ArticleDetailUiState> =
        combine(observeArticle(articleId), loadState) { article, load -> load.toUiState(article) }
            .onStart { fetchIfMissing() }
            .stateInWhileSubscribed(viewModelScope, ArticleDetailUiState())

    fun onIntent(intent: ArticleDetailIntent) {
        when (intent) {
            ArticleDetailIntent.FavoriteToggled -> viewModelScope.launch {
                state.value.article?.let { toggleFavorite(it) }
            }

            ArticleDetailIntent.Retry -> viewModelScope.launch { fetch() }

            ArticleDetailIntent.OpenSource ->
                state.value.article?.sourceUrl?.let(linkHandler::openUrl)

            ArticleDetailIntent.Share -> state.value.article?.let { article ->
                linkHandler.shareText(subject = article.title, text = article.shareText())
            }
        }
    }

    private suspend fun fetchIfMissing() {
        if (observeArticle(articleId).first().isNotNull()) {
            loadState.update(ArticleDetailLoadState::loaded)
            return
        }
        fetch()
    }

    private suspend fun fetch() {
        loadState.update(ArticleDetailLoadState::loading)
        fetchArticle(articleId)
            .onSuccess { loadState.update(ArticleDetailLoadState::loaded) }
            .onFailure { failure -> loadState.update { it.failed(failure) } }
    }
}
