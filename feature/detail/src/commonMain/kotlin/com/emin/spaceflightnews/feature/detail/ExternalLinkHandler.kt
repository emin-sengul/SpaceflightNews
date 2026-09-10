package com.emin.spaceflightnews.feature.detail

import org.koin.core.module.Module

interface ExternalLinkHandler {
    fun openUrl(url: String)
    fun shareText(subject: String, text: String)
}

expect val platformDetailModule: Module
