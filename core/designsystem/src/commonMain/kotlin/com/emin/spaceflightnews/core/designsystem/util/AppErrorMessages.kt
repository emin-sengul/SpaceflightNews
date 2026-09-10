package com.emin.spaceflightnews.core.designsystem.util

import com.emin.spaceflightnews.core.common.AppError
import com.emin.spaceflightnews.core.common.orFallback

fun AppError.toUserMessage(): String = when (this) {
    AppError.NoConnection -> "You appear to be offline. Connect to the internet and try again."
    AppError.Timeout -> "The news service took too long to respond. Please try again."
    is AppError.ClientError -> "That request could not be completed (error $code)."
    is AppError.ServerError -> "The news service is having trouble right now (error $code)."
    AppError.Serialization -> "The news service returned something unexpected."
    is AppError.Unknown -> message.orFallback("Something went wrong. Please try again.")
}

fun AppError.toUserTitle(): String = when (this) {
    AppError.NoConnection -> "No connection"
    AppError.Timeout -> "Request timed out"
    is AppError.ClientError, is AppError.ServerError -> "Service unavailable"
    AppError.Serialization -> "Unexpected response"
    is AppError.Unknown -> "Something went wrong"
}
