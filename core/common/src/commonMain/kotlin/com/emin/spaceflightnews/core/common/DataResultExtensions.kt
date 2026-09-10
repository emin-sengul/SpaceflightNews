package com.emin.spaceflightnews.core.common

inline fun <T, R> DataResult<T>.map(transform: (T) -> R): DataResult<R> = when (this) {
    is DataResult.Success -> DataResult.Success(transform(data))
    is DataResult.Failure -> this
}

inline fun <T, R> DataResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (AppError) -> R,
): R = when (this) {
    is DataResult.Success -> onSuccess(data)
    is DataResult.Failure -> onFailure(error)
}

inline fun <T> DataResult<T>.onSuccess(action: (T) -> Unit): DataResult<T> = apply {
    if (this is DataResult.Success) action(data)
}

inline fun <T> DataResult<T>.onFailure(action: (AppError) -> Unit): DataResult<T> = apply {
    if (this is DataResult.Failure) action(error)
}
