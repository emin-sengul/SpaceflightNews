package com.emin.spaceflightnews.core.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emin.spaceflightnews.core.designsystem.theme.SpaceflightBrand

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val orbitTransition = rememberInfiniteTransition(label = "orbit")
    val orbitRotation by orbitTransition.animateFloat(
        initialValue = -23f,
        targetValue = 337f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "orbitRotation",
    )

    val exhaust by orbitTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 620, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "exhaust",
    )

    val logoScale = remember { Animatable(initialValue = 0.55f) }
    val contentAlpha = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(durationMillis = 420))
    }
    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceflightBrand.gradient),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            BrandLogo(
                orbitRotationDegrees = orbitRotation,
                exhaustScale = exhaust,
                modifier = Modifier
                    .size(168.dp)
                    .scale(logoScale.value)
                    .alpha(contentAlpha.value),
            )

            Text(
                text = "Spaceflight News",
                style = MaterialTheme.typography.headlineSmall,
                color = SpaceflightBrand.Starlight,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .alpha(contentAlpha.value),
            )

            Text(
                text = "Launches, missions and discoveries",
                style = MaterialTheme.typography.bodyMedium,
                color = SpaceflightBrand.Subtitle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .alpha(contentAlpha.value * 0.9f),
            )
        }

        Text(
            text = "Data by Spaceflight News API · The Space Devs",
            fontSize = 11.sp,
            color = SpaceflightBrand.Caption,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp, start = 32.dp, end = 32.dp)
                .alpha(contentAlpha.value),
        )
    }
}

