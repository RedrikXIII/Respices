package com.example.respices.views.elements.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.respices.R
import com.example.respices.support.services.SearchBarManager

@Composable
fun LocalSearchBar(
  placeholder: String,
  options: List<String>,
  exclude: List<String> = listOf(),
  onComplete: (String) -> Unit) {

  var isTyping by remember {mutableStateOf(false) }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start,
    modifier = Modifier
      .clickable (
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
      ) {
        isTyping = true
        SearchBarManager.launchSearchBar(
          options = options,
          exclude = exclude,
          placeholder = placeholder,
          onDone = { final ->
            onComplete.invoke(final)
            isTyping = false
          }
        )
      }
      .fillMaxWidth()
      .padding(vertical = 10.dp)
      .border(
        width = 2.dp,
        color = Color(0xFF000000),
        shape = MaterialTheme.shapes.extraLarge
      )
      .padding(horizontal = 10.dp)
      .height(50.dp)
  ) {
    Image(
      painter = painterResource(R.drawable.outline_search_24),
      contentDescription = "search",
      modifier = Modifier
        .width(25.dp)
        .aspectRatio(1.0f)
    )
    Text(
      text = if (isTyping) SearchBarManager.curInput.value + "|" else placeholder,
      fontSize = 20.sp,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
    )
  }

}