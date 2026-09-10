package com.emin.spaceflightnews.feature.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.emin.spaceflightnews.core.common.isHttpUrl
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidExternalLinkHandler(private val context: Context) : ExternalLinkHandler {
    override fun openUrl(url: String) {
        if (!url.isHttpUrl()) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun shareText(subject: String, text: String) {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = MIME_TYPE_PLAIN_TEXT
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(send, CHOOSER_TITLE)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    private companion object {
        const val MIME_TYPE_PLAIN_TEXT = "text/plain"
        const val CHOOSER_TITLE = "Share article"
    }
}

actual val platformDetailModule: Module = module {
    single<ExternalLinkHandler> { AndroidExternalLinkHandler(get<Context>()) }
}
