package com.emin.spaceflightnews.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object Feed : Destination

    @Serializable
    data object Favorites : Destination

    @Serializable
    data class ArticleDetail(val articleId: Long) : Destination
}
