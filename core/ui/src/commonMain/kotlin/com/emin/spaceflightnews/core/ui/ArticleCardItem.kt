package com.emin.spaceflightnews.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.emin.spaceflightnews.core.designsystem.component.ArticleCard
import com.emin.spaceflightnews.core.designsystem.util.toRelativeLabel
import com.emin.spaceflightnews.core.domain.model.Article

@Composable
fun ArticleCardItem(
    article: Article,
    onClick: (Long) -> Unit,
    onFavoriteClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    val publishedLabel = remember(article.publishedAt) { article.publishedAt.toRelativeLabel() }

    ArticleCard(
        title = article.title,
        summary = article.summary,
        newsSite = article.newsSite,
        publishedLabel = publishedLabel,
        imageUrl = article.imageUrl,
        isFavorite = article.isFavorite,
        onClick = { onClick(article.id) },
        onFavoriteClick = { onFavoriteClick(article) },
        modifier = modifier,
    )
}
