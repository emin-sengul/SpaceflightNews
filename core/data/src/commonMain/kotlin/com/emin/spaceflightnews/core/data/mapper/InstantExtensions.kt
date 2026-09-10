@file:OptIn(ExperimentalTime::class)

package com.emin.spaceflightnews.core.data.mapper

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal fun String.toInstantOrNull(): Instant? = runCatching { Instant.parse(this) }.getOrNull()

internal fun String.toInstantOrEpoch(): Instant = toInstantOrNull() ?: EPOCH

internal fun Instant.toEpochMillis(): Long = toEpochMilliseconds()

internal fun Long.toInstant(): Instant = Instant.fromEpochMilliseconds(this)

private val EPOCH = Instant.fromEpochMilliseconds(0)
