package com.emin.spaceflightnews.core.common

sealed interface AppError {
    data object NoConnection : AppError

    data object Timeout : AppError

    data class ClientError(val code: Int) : AppError

    data class ServerError(val code: Int) : AppError

    data object Serialization : AppError

    data class Unknown(val message: String? = null) : AppError
}
