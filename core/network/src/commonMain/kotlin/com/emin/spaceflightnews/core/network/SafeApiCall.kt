package com.emin.spaceflightnews.core.network

import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.common.DataResult
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException

internal suspend fun <T> safeApiCall(block: suspend () -> T): DataResult<T> = try {
    DataResult.Success(block())
} catch (cancellation: CancellationException) {
    throw cancellation
} catch (clientError: ClientRequestException) {
    DataResult.Failure(AppError.ClientError(clientError.response.status.value))
} catch (serverError: ServerResponseException) {
    DataResult.Failure(AppError.ServerError(serverError.response.status.value))
} catch (timeout: HttpRequestTimeoutException) {
    DataResult.Failure(AppError.Timeout)
} catch (serialization: SerializationException) {
    DataResult.Failure(AppError.Serialization)
} catch (throwable: Throwable) {
    val error = when {
        throwable.isConnectivityError() -> AppError.NoConnection
        throwable.cause is SerializationException -> AppError.Serialization
        else -> AppError.Unknown(throwable.message)
    }
    DataResult.Failure(error)
}

internal expect fun Throwable.isConnectivityError(): Boolean
