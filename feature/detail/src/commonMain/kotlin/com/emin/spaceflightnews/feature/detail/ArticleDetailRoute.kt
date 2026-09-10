package com.emin.spaceflightnews.feature.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ArticleDetailRoute(
    articleId: Long,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
    viewModel: ArticleDetailViewModel = koinViewModel(
        key = VIEW_MODEL_KEY_PREFIX + articleId,
    ) { parametersOf(articleId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onIntent = remember(viewModel) { viewModel::onIntent }

    ArticleDetailScreen(
        state = state,
        onIntent = onIntent,
        onBack = onBack,
        modifier = modifier,
    )
}

private const val VIEW_MODEL_KEY_PREFIX = "article-detail-"
