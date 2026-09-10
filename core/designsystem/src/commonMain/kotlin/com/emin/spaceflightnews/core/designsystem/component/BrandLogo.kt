package com.emin.spaceflightnews.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import com.emin.spaceflightnews.core.designsystem.theme.SpaceflightBrand

@Composable
fun BrandLogo(
    modifier: Modifier = Modifier,
    orbitRotationDegrees: Float = -23f,
    exhaustScale: Float = 1f,
) {
    Canvas(modifier = modifier) {
        drawBrandMark(
            orbitRotationDegrees = orbitRotationDegrees,
            exhaustScale = exhaustScale,
        )
    }
}

private const val CANVAS_UNITS = 108f

private val BodyColor = SpaceflightBrand.Starlight
private val WindowColor = SpaceflightBrand.OrbitTealLight
private val WindowInnerColor = SpaceflightBrand.WindowGlass
private val FinColor = SpaceflightBrand.EmberLight
private val ExhaustColor = SpaceflightBrand.Exhaust
private val OrbitColor = SpaceflightBrand.OrbitTealLight

private fun DrawScope.drawBrandMark(
    orbitRotationDegrees: Float,
    exhaustScale: Float,
) {
    val unit = size.minDimension / CANVAS_UNITS
    val originX = (size.width - CANVAS_UNITS * unit) / 2f
    val originY = (size.height - CANVAS_UNITS * unit) / 2f

    translate(left = originX, top = originY) {
        scale(scaleX = unit, scaleY = unit, pivot = Offset.Zero) {
            rotate(degrees = orbitRotationDegrees, pivot = Offset(54f, 54f)) {
                drawOval(
                    color = OrbitColor.copy(alpha = 0.65f),
                    topLeft = Offset(25f, 41.5f),
                    size = Size(width = 58f, height = 25f),
                    style = Stroke(width = 2.4f),
                )
            }

            scale(scaleX = 1f, scaleY = exhaustScale, pivot = Offset(54f, 71f)) {
                drawPath(triangle(49.5f, 71f, 54f, 84f, 58.5f, 71f), ExhaustColor)
                drawPath(
                    triangle(51.8f, 71f, 54f, 80f, 56.2f, 71f),
                    Color.White.copy(alpha = 0.85f),
                )
            }

            drawPath(triangle(43.5f, 60.5f, 35f, 77f, 43.5f, 71.5f), FinColor)
            drawPath(triangle(64.5f, 60.5f, 73f, 77f, 64.5f, 71.5f), FinColor)

            drawPath(rocketBody(), BodyColor)

            drawCircle(WindowColor, radius = 5.4f, center = Offset(54f, 49.5f))
            drawCircle(WindowInnerColor, radius = 3.2f, center = Offset(54f, 49.5f))
        }
    }
}

private fun rocketBody(): Path = Path().apply {
    moveTo(54f, 30f)
    cubicTo(60f, 39f, 64.5f, 49f, 64.5f, 58f)
    cubicTo(64.5f, 63.5f, 62f, 68f, 59.5f, 71f)
    lineTo(48.5f, 71f)
    cubicTo(46f, 68f, 43.5f, 63.5f, 43.5f, 58f)
    cubicTo(43.5f, 49f, 48f, 39f, 54f, 30f)
    close()
}

private fun triangle(
    x1: Float, y1: Float,
    x2: Float, y2: Float,
    x3: Float, y3: Float,
): Path = Path().apply {
    moveTo(x1, y1)
    lineTo(x2, y2)
    lineTo(x3, y3)
    close()
}
