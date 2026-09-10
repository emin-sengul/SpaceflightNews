package com.emin.spaceflightnews.core.network

import io.ktor.client.engine.darwin.DarwinHttpRequestException

internal actual fun Throwable.isConnectivityError(): Boolean = this is DarwinHttpRequestException
