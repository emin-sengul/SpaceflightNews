package com.emin.spaceflightnews.core.common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun String?.isNotNullOrBlank(): Boolean {
    contract { returns(true) implies (this@isNotNullOrBlank != null) }
    return !isNullOrBlank()
}

fun String?.takeIfNotBlank(): String? = this?.takeIf(String::isNotBlank)

fun String?.orFallback(fallback: String): String = takeIfNotBlank() ?: fallback

fun String.appendIfNotBlank(other: String, separator: String): String = when {
    other.isBlank() -> this
    isBlank() -> other
    else -> this + separator + other
}

fun Iterable<String>.joinNotBlank(separator: String = ", "): String =
    filter(String::isNotBlank).joinToString(separator)

fun String.trimLeadingZeros(): String = trimStart('0')

fun String?.isHttpUrl(): Boolean {
    val value = takeIfNotBlank() ?: return false
    return HTTP_SCHEMES.any { scheme -> value.startsWith(scheme, ignoreCase = true) }
}

fun String?.takeIfHttpUrl(): String? = takeIf { it.isHttpUrl() }

private val HTTP_SCHEMES = listOf("http://", "https://")
