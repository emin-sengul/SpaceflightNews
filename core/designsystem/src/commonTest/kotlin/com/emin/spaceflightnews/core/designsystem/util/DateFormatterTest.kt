@file:OptIn(ExperimentalTime::class)

package com.emin.spaceflightnews.core.designsystem.util

import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class DateFormatterTest {
    private val now = Instant.parse("2026-09-08T12:00:00Z")

    @Test
    fun showsJustNowForTheLastMinute() {
        assertEquals("Just now", (now - 30.seconds).toRelativeLabel(now, TimeZone.UTC))
    }

    @Test
    fun showsMinutesWithinTheHour() {
        assertEquals("12m ago", (now - 12.minutes).toRelativeLabel(now, TimeZone.UTC))
    }

    @Test
    fun showsHoursWithinTheDay() {
        assertEquals("5h ago", (now - 5.hours).toRelativeLabel(now, TimeZone.UTC))
    }

    @Test
    fun showsDaysWithinTheWeek() {
        assertEquals("3d ago", (now - 3.days).toRelativeLabel(now, TimeZone.UTC))
    }

    @Test
    fun fallsBackToAnAbsoluteDateForOlderArticles() {
        assertEquals("1 Sep 2026", (now - 7.days).toRelativeLabel(now, TimeZone.UTC))
    }

    @Test
    fun formatsAbsoluteDatesWithoutALeadingZero() {
        val date = Instant.parse("2026-09-07T23:30:00Z")
        assertEquals("7 Sep 2026", date.toAbsoluteDateLabel(TimeZone.UTC))
    }

    @Test
    fun handlesAClockThatIsBehindThePublisher() {
        val future = now + 10.minutes
        assertEquals("8 Sep 2026", future.toRelativeLabel(now, TimeZone.UTC))
    }
}
