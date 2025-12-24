package com.example.respices.views.screens

import android.content.ClipData
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.R
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.views.elements.input.IconButton
import kotlinx.coroutines.launch

@Composable
fun FileView(recipeViewModel: RecipeViewModel = viewModel()) {
  RespicesTheme {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val clipboard: Clipboard = LocalClipboard.current

    val initialFile = remember { mutableStateOf("") }
    val currentFile = remember { mutableStateOf("") }

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    LaunchedEffect(Unit) {
      initialFile.value = "smth smth smth dataaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                          "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                          "d\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\n" +
                          "d\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\nd\n" +
                          "end of text file"
      currentFile.value = initialFile.value
    }

    LaunchedEffect(currentFile.value) {
      //TODO update database
      Log.d("file view test", "new file: ${currentFile.value}")
    }

    Text(
      text = "Meals List File",
      fontSize = 32.sp,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 30.dp)
    )

    SelectionContainer {
      Text(
        text = "COPY FROM HERE",
        fontSize = 28.sp
      )
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
        .border(
          color = Color.Black,
          width = 1.dp
        )
        .padding(10.dp)
        .verticalScroll(rememberScrollState())
        .horizontalScroll(rememberScrollState())
    ) {
      Text(
        text = currentFile.value,
        fontSize = 18.sp
      )
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 40.dp)
        .padding(horizontal = 10.dp)
        .height(90.dp)
    ) {
      IconButton(
        R.drawable.copy,
        modifier = Modifier
          .padding(horizontal = 10.dp)
          .clickable {
            scope.launch {
              clipboard.setClipEntry(
                ClipEntry(
                  ClipData.newPlainText(
                    "meals file",
                    currentFile.value
                  )
                )
              )
            }
          },
        imageModifier = Modifier
          .padding(20.dp)
      )

      IconButton(
        R.drawable.paste,
        modifier = Modifier
          .padding(horizontal = 10.dp)
          .clickable {
            scope.launch {
              var res = ""
              clipboard.getClipEntry()?.clipData?.let { data ->
                if (data.itemCount > 0) {
                  res = data.getItemAt(0)
                    .coerceToText(context)
                    .toString()
                }
              }

              currentFile.value = res
            }
          },
        imageModifier = Modifier
          .padding(20.dp)
      )

      IconButton(
        R.drawable.circular_arrow,
        modifier = Modifier
          .padding(horizontal = 10.dp)
          .clickable {
            currentFile.value = initialFile.value
          },
        imageModifier = Modifier
          .padding(20.dp)
      )
    }
  }
}