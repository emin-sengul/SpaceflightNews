package com.emin.spaceflightnews.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher = ioDispatcher
    override val default: CoroutineDispatcher = Dispatchers.Default
}
