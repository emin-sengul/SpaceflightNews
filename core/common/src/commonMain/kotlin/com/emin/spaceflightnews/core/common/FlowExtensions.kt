package com.emin.spaceflightnews.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

const val UI_STATE_STOP_TIMEOUT_MILLIS = 5_000L

fun <T> Flow<T>.stateInWhileSubscribed(
    scope: CoroutineScope,
    initialValue: T,
    stopTimeoutMillis: Long = UI_STATE_STOP_TIMEOUT_MILLIS,
): StateFlow<T> = stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(stopTimeoutMillis),
    initialValue = initialValue,
)
