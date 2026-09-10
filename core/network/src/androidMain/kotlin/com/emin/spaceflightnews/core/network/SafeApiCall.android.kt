package com.emin.spaceflightnews.core.network

import java.io.IOException

internal actual fun Throwable.isConnectivityError(): Boolean = this is IOException
