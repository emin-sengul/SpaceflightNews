package com.emin.spaceflightnews.navigation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.emin.spaceflightnews.feature.detail.ArticleDetailRoute
import com.emin.spaceflightnews.feature.favorites.FavoritesRoute
import com.emin.spaceflightnews.ui.FeedPane

@Composable
internal fun SpaceflightNavHost(
    navController: NavHostController,
    feedListState: LazyListState,
    favoritesListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val onArticleClick = remember(navController) { navController::navigateToArticle }
    val onBack: () -> Unit = remember(navController) {
        { navController.popBackStack() }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Feed,
        modifier = modifier,
        enterTransition = { forwardEnterTransition() },
        exitTransition = { forwardExitTransition() },
        popEnterTransition = { backEnterTransition() },
        popExitTransition = { backExitTransition() },
    ) {
        composable<Destination.Feed> {
            FeedPane(listState = feedListState, onArticleClick = onArticleClick)
        }

        composable<Destination.Favorites> {
            FavoritesRoute(listState = favoritesListState, onArticleClick = onArticleClick)
        }

        composable<Destination.ArticleDetail> { entry ->
            val route = entry.toRoute<Destination.ArticleDetail>()
            ArticleDetailRoute(articleId = route.articleId, onBack = onBack)
        }
    }
}
