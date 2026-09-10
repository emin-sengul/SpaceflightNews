package com.emin.spaceflightnews.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

internal val SpaceflightLightColorScheme = lightColorScheme(
    primary = SpaceflightBrand.MissionBlue,
    onPrimary = SpaceflightBrand.OnBrand,
    primaryContainer = SpaceflightBrand.MissionBlueContainer,
    onPrimaryContainer = SpaceflightBrand.MissionBlueInk,
    secondary = SpaceflightBrand.OrbitTeal,
    onSecondary = SpaceflightBrand.OnBrand,
    secondaryContainer = SpaceflightBrand.OrbitTealContainer,
    onSecondaryContainer = SpaceflightBrand.OrbitTealInk,
    tertiary = SpaceflightBrand.Ember,
    onTertiary = SpaceflightBrand.OnBrand,
    background = SpaceflightBrand.Starlight,
    onBackground = SpaceflightBrand.Ink,
    surface = SpaceflightBrand.StarlightSurface,
    onSurface = SpaceflightBrand.Ink,
    surfaceVariant = SpaceflightBrand.StarlightSurfaceHigh,
    onSurfaceVariant = SpaceflightBrand.InkVariant,
    outline = SpaceflightBrand.StarlightOutline,
    outlineVariant = SpaceflightBrand.StarlightOutlineVariant,
    error = SpaceflightBrand.ErrorRed,
    onError = SpaceflightBrand.OnBrand,
    errorContainer = SpaceflightBrand.ErrorContainer,
    onErrorContainer = SpaceflightBrand.ErrorInk,
)

internal val SpaceflightDarkColorScheme = darkColorScheme(
    primary = SpaceflightBrand.MissionBlueLight,
    onPrimary = SpaceflightBrand.MissionBlueOnDark,
    primaryContainer = SpaceflightBrand.MissionBlueDeep,
    onPrimaryContainer = SpaceflightBrand.MissionBlueContainer,
    secondary = SpaceflightBrand.OrbitTealLight,
    onSecondary = SpaceflightBrand.OrbitTealOnDark,
    secondaryContainer = SpaceflightBrand.OrbitTealDeep,
    onSecondaryContainer = SpaceflightBrand.OrbitTealContainer,
    tertiary = SpaceflightBrand.EmberLight,
    onTertiary = SpaceflightBrand.EmberInk,
    background = SpaceflightBrand.Night,
    onBackground = SpaceflightBrand.Chalk,
    surface = SpaceflightBrand.NightSurface,
    onSurface = SpaceflightBrand.Chalk,
    surfaceVariant = SpaceflightBrand.NightSurfaceHigh,
    onSurfaceVariant = SpaceflightBrand.ChalkVariant,
    outline = SpaceflightBrand.NightOutline,
    outlineVariant = SpaceflightBrand.InkVariant,
    error = SpaceflightBrand.ErrorRedLight,
    onError = SpaceflightBrand.ErrorInkDark,
    errorContainer = SpaceflightBrand.ErrorContainerDark,
    onErrorContainer = SpaceflightBrand.ErrorChalk,
)
