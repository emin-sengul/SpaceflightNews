package com.emin.spaceflightnews.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlin.reflect.KClass

internal fun NavDestination?.isOn(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true

internal fun NavDestination?.isTopLevel(): Boolean =
    isOn(Destination.Feed::class) || isOn(Destination.Favorites::class)

internal fun NavHostController.navigateToTopLevel(destination: Destination) {
    navigate(destination) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

internal fun NavHostController.navigateToArticle(articleId: Long) {
    navigate(Destination.ArticleDetail(articleId))
}
