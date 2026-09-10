package com.emin.spaceflightnews.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponseDto<T>(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<T> = emptyList(),
)
