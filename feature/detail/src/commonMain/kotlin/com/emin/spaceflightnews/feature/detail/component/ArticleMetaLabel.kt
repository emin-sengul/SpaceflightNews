package com.emin.spaceflightnews.feature.detail.component

import com.emin.spaceflightnews.core.common.appendIfNotBlank
import com.emin.spaceflightnews.core.designsystem.util.toAbsoluteDateLabel
import com.emin.spaceflightnews.core.domain.model.Article
import com.emin.spaceflightnews.core.domain.model.authorsLabel

internal fun Article.metaLabel(): String =
    publishedAt.toAbsoluteDateLabel().appendIfNotBlank(authorsLabel(), SEPARATOR)

private const val SEPARATOR = "  ·  "
