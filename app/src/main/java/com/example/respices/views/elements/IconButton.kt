package com.example.respices.views.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.respices.R
import com.example.respices.ui.theme.RespicesTheme

@Composable
fun IconButton(
  icon: Int?,
  modifier: Modifier = Modifier
) {
  RespicesTheme {
    Box (
      modifier = modifier
        .border(
          width = 2.dp,
          color = Color(0xFF000000),
          shape = MaterialTheme.shapes.small
        )
        .padding(2.dp)
        .fillMaxHeight()
        .aspectRatio(1.0f)
        .clip(MaterialTheme.shapes.small)
    ) {
      Image(
        painter = painterResource(icon ?: R.drawable.baseline_question_mark_24),
        contentDescription = "Icon!",
        modifier = Modifier
          .fillMaxHeight()
          .align(Alignment.Center)
      )
    }
  }
}