package com.emin.spaceflightnews.feature.detail

import com.emin.spaceflightnews.core.common.isHttpUrl
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

class IosExternalLinkHandler : ExternalLinkHandler {
    override fun openUrl(url: String) {
        if (!url.isHttpUrl()) return
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }

    override fun shareText(subject: String, text: String) {
        val controller = UIActivityViewController(
            activityItems = listOf(text),
            applicationActivities = null,
        )
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            controller,
            animated = true,
            completion = null,
        )
    }
}

actual val platformDetailModule: Module = module {
    single<ExternalLinkHandler> { IosExternalLinkHandler() }
}
