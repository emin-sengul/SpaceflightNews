package com.emin.spaceflightnews.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    val name: String = "",
)
