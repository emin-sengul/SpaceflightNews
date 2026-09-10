package com.emin.spaceflightnews.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emin.spaceflightnews.core.common.isNotNull
import com.emin.spaceflightnews.core.designsystem.component.ErrorState
import com.emin.spaceflightnews.core.designsystem.component.LoadingState
import com.emin.spaceflightnews.core.designsystem.util.toUserMessage
import com.emin.spaceflightnews.core.designsystem.util.toUserTitle
import com.emin.spaceflightnews.feature.detail.component.ArticleDetailContent
import com.emin.spaceflightnews.feature.detail.component.DetailTopActions

@Composable
fun ArticleDetailScreen(
    state: ArticleDetailUiState,
    onIntent: (ArticleDetailIntent) -> Unit,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> LoadingState()

            state.error.isNotNull() -> ErrorState(
                title = state.error.toUserTitle(),
                message = state.error.toUserMessage(),
                onRetry = { onIntent(ArticleDetailIntent.Retry) },
            )

            state.article.isNotNull() -> ArticleDetailContent(
                article = state.article,
                onOpenSource = { onIntent(ArticleDetailIntent.OpenSource) },
            )
        }

        DetailTopActions(
            isFavorite = state.article?.isFavorite,
            onBack = onBack,
            onShare = { onIntent(ArticleDetailIntent.Share) },
            onFavoriteToggle = { onIntent(ArticleDetailIntent.FavoriteToggled) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
        )
    }
}
