package com.emin.spaceflightnews.core.network

import com.emin.spaceflightnews.core.network.api.SpaceflightNewsApi
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(enableLogging: Boolean = true): HttpClient = HttpClient {
    installSpaceflightNewsDefaults(enableLogging)
}

internal fun HttpClientConfig<*>.installSpaceflightNewsDefaults(enableLogging: Boolean) {
    expectSuccess = true

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
                explicitNulls = false
            }
        )
    }

    install(HttpTimeout) {
        requestTimeoutMillis = REQUEST_TIMEOUT_MS
        connectTimeoutMillis = CONNECT_TIMEOUT_MS
        socketTimeoutMillis = REQUEST_TIMEOUT_MS
    }

    if (enableLogging) {
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    defaultRequest {
        url(SpaceflightNewsApi.BASE_URL)
        accept(ContentType.Application.Json)
    }
}

private const val REQUEST_TIMEOUT_MS = 30_000L
private const val CONNECT_TIMEOUT_MS = 15_000L
