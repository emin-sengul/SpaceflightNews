@file:OptIn(ExperimentalTime::class)

package com.emin.spaceflightnews.core.designsystem.util

import com.emin.spaceflightnews.core.common.orFallback
import com.emin.spaceflightnews.core.common.trimLeadingZeros
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toRelativeLabel(
    now: Instant = Clock.System.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String {
    val elapsed = now - this
    return when {
        elapsed.isNegative() -> toAbsoluteDateLabel(timeZone)
        elapsed.inWholeMinutes < 1 -> "Just now"
        elapsed.inWholeHours < 1 -> elapsed.inWholeMinutes.toString() + "m ago"
        elapsed.inWholeDays < 1 -> elapsed.inWholeHours.toString() + "h ago"
        elapsed.inWholeDays < DAYS_IN_WEEK -> elapsed.inWholeDays.toString() + "d ago"
        else -> toAbsoluteDateLabel(timeZone)
    }
}

fun Instant.toAbsoluteDateLabel(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String {
    val isoDate = toLocalDateTime(timeZone).date.toString()

    val parts = isoDate.split(ISO_SEPARATOR)
    if (parts.size != ISO_PART_COUNT) return isoDate

    val year = parts[0]
    val monthIndex = parts[1].toIntOrNull()?.minus(1) ?: return isoDate
    val day = parts[2].trimLeadingZeros().orFallback(FIRST_DAY_OF_MONTH)
    val month = MONTH_ABBREVIATIONS.getOrNull(monthIndex) ?: return isoDate

    return day + " " + month + " " + year
}

private const val FIRST_DAY_OF_MONTH = "1"
private const val DAYS_IN_WEEK = 7
private const val ISO_SEPARATOR = "-"
private const val ISO_PART_COUNT = 3

private val MONTH_ABBREVIATIONS = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
)
