package com.emin.spaceflightnews.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.emin.spaceflightnews.core.designsystem.util.scrollToTop
import com.emin.spaceflightnews.navigation.Destination
import com.emin.spaceflightnews.navigation.SpaceflightNavHost
import com.emin.spaceflightnews.navigation.isOn
import com.emin.spaceflightnews.navigation.isTopLevel
import com.emin.spaceflightnews.navigation.navigateToTopLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun MainScaffold(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val coroutineScope = rememberCoroutineScope()

    val feedListState = rememberLazyListState()
    val favoritesListState = rememberLazyListState()

    val isFeedSelected = currentDestination.isOn(Destination.Feed::class)
    val isFavoritesSelected = currentDestination.isOn(Destination.Favorites::class)

    val onFeedClick = rememberTopLevelTabClick(
        destination = Destination.Feed,
        selected = isFeedSelected,
        listState = feedListState,
        navController = navController,
        coroutineScope = coroutineScope,
    )

    val onFavoritesClick = rememberTopLevelTabClick(
        destination = Destination.Favorites,
        selected = isFavoritesSelected,
        listState = favoritesListState,
        navController = navController,
        coroutineScope = coroutineScope,
    )

    Scaffold(modifier = modifier) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            SpaceflightNavHost(
                navController = navController,
                feedListState = feedListState,
                favoritesListState = favoritesListState,
                modifier = Modifier.fillMaxSize(),
            )

            AnimatedVisibility(
                visible = currentDestination.isTopLevel(),
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 },
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                FloatingTabBar(
                    isFeedSelected = isFeedSelected,
                    isFavoritesSelected = isFavoritesSelected,
                    onFeedClick = onFeedClick,
                    onFavoritesClick = onFavoritesClick,
                )
            }
        }
    }
}

@Composable
private fun rememberTopLevelTabClick(
    destination: Destination,
    selected: Boolean,
    listState: LazyListState,
    navController: NavHostController,
    coroutineScope: CoroutineScope,
): () -> Unit = remember(destination, selected, listState, navController, coroutineScope) {
    {
        if (selected) {
            coroutineScope.launch { listState.scrollToTop() }
        } else {
            navController.navigateToTopLevel(destination)
        }
    }
}
