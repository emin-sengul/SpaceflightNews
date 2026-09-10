package com.emin.spaceflightnews.core.designsystem.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object SpaceflightBrand {

    val MissionBlue = Color(0xFF2E5AAC)
    val MissionBlueLight = Color(0xFFA8C6FF)
    val MissionBlueDeep = Color(0xFF14458F)
    val MissionBlueContainer = Color(0xFFD8E3FF)
    val MissionBlueInk = Color(0xFF001B3F)
    val MissionBlueOnDark = Color(0xFF002F65)

    val OrbitTeal = Color(0xFF1F6F6B)
    val OrbitTealLight = Color(0xFF7DD8D2)
    val OrbitTealContainer = Color(0xFFCDEFEC)
    val OrbitTealInk = Color(0xFF00201E)
    val OrbitTealDeep = Color(0xFF00504C)
    val OrbitTealOnDark = Color(0xFF003734)

    val Ember = Color(0xFFB4501F)
    val EmberLight = Color(0xFFFFB68F)
    val EmberInk = Color(0xFF5B1A00)
    val Exhaust = Color(0xFFFFD166)

    val DeepSpace = Color(0xFF0B1020)
    val Night = Color(0xFF0F131B)
    val NightSurface = Color(0xFF171C26)
    val NightSurfaceHigh = Color(0xFF222834)
    val NightOutline = Color(0xFF8E9199)
    val WindowGlass = Color(0xFF1E3C6E)

    val Starlight = Color(0xFFF7F9FC)
    val StarlightSurface = Color(0xFFFFFFFF)
    val StarlightSurfaceHigh = Color(0xFFEDF1F7)
    val StarlightOutline = Color(0xFF74777F)
    val StarlightOutlineVariant = Color(0xFFC4C6CF)

    val Ink = Color(0xFF1A1C20)
    val InkVariant = Color(0xFF44474E)
    val Chalk = Color(0xFFE3E6ED)
    val ChalkVariant = Color(0xFFC4C6CF)

    val Subtitle = Color(0xFF9FB4D8)
    val Caption = Color(0xFF6F82A6)

    val ErrorRed = Color(0xFFB3261E)
    val ErrorRedLight = Color(0xFFFFB4AB)
    val ErrorContainer = Color(0xFFF9DEDC)
    val ErrorContainerDark = Color(0xFF93000A)
    val ErrorInk = Color(0xFF410E0B)
    val ErrorInkDark = Color(0xFF690005)
    val ErrorChalk = Color(0xFFFFDAD6)

    val OnBrand = Color.White

    val gradient: Brush = Brush.linearGradient(colors = listOf(MissionBlueDeep, DeepSpace))
}
