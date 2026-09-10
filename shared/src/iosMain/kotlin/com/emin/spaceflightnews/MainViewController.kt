package com.emin.spaceflightnews

import androidx.compose.ui.window.ComposeUIViewController
import com.emin.spaceflightnews.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { startKoinOnce() },
) {
    SpaceflightNewsApp()
}

private var koinStarted = false

private fun startKoinOnce() {
    if (koinStarted) return
    initKoin()
    koinStarted = true
}
