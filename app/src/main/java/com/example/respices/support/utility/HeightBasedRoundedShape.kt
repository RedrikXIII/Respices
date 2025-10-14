package com.example.respices.support.utility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class HeightBasedRoundedShape : Shape {
  override fun createOutline(
    size: androidx.compose.ui.geometry.Size,
    layoutDirection: LayoutDirection,
    density: Density
  ): Outline {
    val radius = size.height / 2f
    val roundRect = RoundRect(
      left = 0f,
      top = 0f,
      right = size.width,
      bottom = size.height,
      topLeftCornerRadius = CornerRadius(radius, radius),
      topRightCornerRadius = CornerRadius(radius, radius),
      bottomLeftCornerRadius = CornerRadius(radius, radius),
      bottomRightCornerRadius = CornerRadius(radius, radius)
    )
    return Outline.Generic(Path().apply { addRoundRect(roundRect) })
  }
}