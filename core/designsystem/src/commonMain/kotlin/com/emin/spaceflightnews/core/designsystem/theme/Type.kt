package com.emin.spaceflightnews.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal val SpaceflightTypography = Typography().let { defaults ->
    defaults.copy(
        headlineSmall = defaults.headlineSmall.copy(
            fontWeight = FontWeight.SemiBold,
            lineHeight = 32.sp,
        ),
        titleMedium = TextStyle(
            fontSize = 17.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.SemiBold,
        ),
        bodyMedium = defaults.bodyMedium.copy(
            lineHeight = 22.sp,
        ),
        labelSmall = defaults.labelSmall.copy(
            fontWeight = FontWeight.Medium,
        ),
    )
}
