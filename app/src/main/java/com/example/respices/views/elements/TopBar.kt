package com.example.respices.views.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.respices.support.enums.Screen
import com.example.respices.support.services.GlobalLookup
import com.example.respices.support.services.GlobalState
import com.example.respices.ui.theme.RespicesTheme

@Composable
fun TopBar() {
  RespicesTheme {
    Row (
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .background(Color.Red)
        .fillMaxWidth()
        .height(100.dp)
        .padding(10.dp)
    ) {
      Text (
        text = "Re'Spices",
        fontStyle = FontStyle.Normal,
        fontSize = 28.sp,
        fontWeight = FontWeight.Black
      )

      Spacer(
        modifier = Modifier
          .weight(1f)
      )

      val curScreen: Screen by GlobalState.currentScreen
      val panel = GlobalLookup.screenToTopBarScreens[curScreen]
      Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
          .wrapContentWidth()
          .height(60.dp)
      ) {
        IconButton(
          panel?.first?.second,
          modifier = Modifier
            .clickable {
              panel?.first?.let { GlobalState.setCurrentScreen(it.first) }
            }
        )
        IconButton(
          panel?.second?.second,
          modifier = Modifier
            .clickable {
              panel?.second?.let { GlobalState.setCurrentScreen(it.first) }
            }
        )
        IconButton(
          panel?.third?.second,
          modifier = Modifier
            .clickable {
              panel?.third?.let { GlobalState.setCurrentScreen(it.first) }
            }
        )
      }

    }
  }
}
