package com.example.respices.views.elements.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.respices.R
import com.example.respices.support.extensions.getTopNSimilarity
import com.example.respices.support.services.SearchBarManager
import com.example.respices.support.utility.HeightBasedRoundedShape
import com.example.respices.ui.theme.RespicesTheme

@Composable
fun GlobalSearchBar() {
  val options = SearchBarManager.curOptions
  val placeholder = SearchBarManager.curPlaceholder
  val curInput by SearchBarManager.curInput
  val isActive = SearchBarManager.isActive

  val focusRequester = remember { FocusRequester() }

  RespicesTheme {
    if (isActive) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .background(Color(0xDDFFFFFF))
          .fillMaxWidth()
          .fillMaxHeight()
          .padding(vertical = 12.dp, horizontal = 30.dp)
          .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
          ) {
            SearchBarManager.closeSearchBar()
          }
      ) {
        OutlinedTextField(
          value = curInput,
          onValueChange = {
            SearchBarManager.curInput.value = it
          },
          singleLine = true,
          leadingIcon = {
            Image(
              painter = painterResource(R.drawable.outline_search_24),
              contentDescription = "Search",
              modifier = Modifier
                .height(30.dp)
                .padding(start = 15.dp, end = 5.dp)
                .aspectRatio(1.0f)
            )
          },
          placeholder = {
            Text(
              text = placeholder,
              fontSize = 20.sp
            )
          },
          shape = HeightBasedRoundedShape(),
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            showKeyboardOnFocus = true
          ),
          keyboardActions = KeyboardActions(onDone = {
            SearchBarManager.closeSearchBar()
          }),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
          ),
          textStyle = TextStyle(
            fontSize = 20.sp
          ),
          modifier = Modifier
            .focusRequester(focusRequester)
            .background(
              color = Color.White,
              shape = HeightBasedRoundedShape()
            )
            .fillMaxWidth()
            .height(60.dp)
        )

        if (curInput.isNotEmpty()) {
          LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
              .fillMaxWidth()
          ) {
            items(options.getTopNSimilarity(3, curInput)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .clickable {
                    SearchBarManager.curInput.value = it
                    SearchBarManager.closeSearchBar()
                  }
                  .wrapContentWidth()
                  .height(55.dp)
                  .padding(top = 15.dp, start = 20.dp)
                  .border(
                    width = 2.dp,
                    color = Color(0xFF000000),
                    shape = HeightBasedRoundedShape()
                  )
                  .background(
                    color = Color.White,
                    shape = HeightBasedRoundedShape()
                  )
                  .padding(horizontal = 10.dp)
                ) {
                Image(
                  painter = painterResource(R.drawable.baseline_circle_24),
                  contentDescription = "Dot",
                  modifier = Modifier
                    .padding(start = 5.dp)
                    .height(17.dp)
                    .aspectRatio(1.0f)
                    .padding(end = 10.dp)
                )
                Text(
                  text = it,
                  fontSize = 24.sp,
                  modifier = Modifier
                    .padding(end = 5.dp)
                )
              }
            }
          }
        }

        LaunchedEffect(Unit) {
          focusRequester.requestFocus()
        }
      }
    }
  }
}