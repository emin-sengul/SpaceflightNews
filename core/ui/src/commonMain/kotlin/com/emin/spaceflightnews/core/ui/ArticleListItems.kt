package com.emin.spaceflightnews.core.ui

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.emin.spaceflightnews.core.domain.model.Article

fun LazyListScope.articleItems(
    articles: List<Article>,
    onArticleClick: (Long) -> Unit,
    onFavoriteClick: (Article) -> Unit,
) {
    items(
        items = articles,
        key = { it.id },
        contentType = { ListContentType.ARTICLE },
    ) { article ->
        ArticleCardItem(
            article = article,
            onClick = onArticleClick,
            onFavoriteClick = onFavoriteClick,
            modifier = Modifier.animateItem(),
        )
    }
}

fun LazyListScope.headerItem(key: String, content: @Composable () -> Unit) {
    item(key = key, contentType = ListContentType.HEADER) { content() }
}

fun LazyListScope.footerItem(key: String, content: @Composable () -> Unit) {
    item(key = key, contentType = ListContentType.FOOTER) { content() }
}
