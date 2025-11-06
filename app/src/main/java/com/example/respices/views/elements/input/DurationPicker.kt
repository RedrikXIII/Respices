package com.example.respices.views.elements.input

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.respices.R
import com.example.respices.support.extensions.replaceTyping
import com.example.respices.ui.theme.RespicesTheme
import kotlin.math.max
import kotlin.math.min

@Composable
fun DurationPicker(
  initialTime: Long,
  onConfirm: (Long) -> Unit
) {
  var curTime by remember { mutableLongStateOf(initialTime) }

  var hoursFieldValue by remember { mutableStateOf(TextFieldValue("0")) }
  var minutesFieldValue by remember { mutableStateOf(TextFieldValue("00")) }

  var curHours by remember { mutableStateOf("0") }
  var curMinutes by remember { mutableStateOf("00") }

  val secondFocusRequester = remember { FocusRequester() }

  val focusManager = LocalFocusManager.current

  var isFocused by remember { mutableStateOf(false) }
  var isFocused2 by remember { mutableStateOf(false) }
  RespicesTheme {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .padding(top = 20.dp, bottom = 10.dp)
    ) {
      Image(
        painter = painterResource(R.drawable.outline_alarm_24),
        contentDescription = "clock icon",
        modifier = Modifier
          .padding(end = 20.dp)
          .height(60.dp)
          .aspectRatio(1.0f)
      )

      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .height(60.dp)
          .width(70.dp)
          .graphicsLayer { clip = true }
          .border(
            color = Color.Black,
            width = 1.dp
          )
          .padding(all = 10.dp)
      ) {
        OutlinedTextField(
          value = hoursFieldValue,
          onValueChange = { newInput ->
            newInput.text.toLongOrNull()?.let {
              val str: String = newInput.text
              curHours = curHours.replaceTyping(str)

              if (curHours.length > 2) {
                curHours = curHours.substring(startIndex = 0, endIndex = 2)
              }
              curHours.toLongOrNull()?.let {
                val nit = max(min(it, 23L), 0L)
                curTime = nit * 60 + curTime.mod(60)
                curHours = nit.toString()
              }
              hoursFieldValue = newInput.copy(text = curHours)
              if (newInput.selection.start >= 2) {
                hoursFieldValue = hoursFieldValue.copy(selection = TextRange(0, 0))
                minutesFieldValue = minutesFieldValue.copy(selection = TextRange(0, 0))
                onConfirm(curTime)
                secondFocusRequester.requestFocus()
              }
            }
          },
          keyboardOptions = KeyboardOptions(
            showKeyboardOnFocus = true,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
          ),
          textStyle = TextStyle(
            fontSize = 31.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
          ),
          placeholder = { Text(text = curHours) },
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.Black
          ),
          modifier = Modifier
            .onFocusChanged { focusState: FocusState ->
              if (isFocused && !focusState.isFocused) {
                onConfirm.invoke(curTime)
              }
              isFocused = focusState.isFocused
            }
            .wrapContentSize(unbounded = true)
            .width(100.dp)
            .height(100.dp)
        )
      }

      Text(
        text = "h:",
        fontWeight = FontWeight.Bold,
        fontSize = 31.sp,
        modifier = Modifier
          .padding(
            start = 10.dp,
            end = 15.dp
          )
      )

      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .height(60.dp)
          .width(70.dp)
          .graphicsLayer { clip = true }
          .border(
            color = Color.Black,
            width = 1.dp
          )
          .padding(all = 10.dp)
      ) {
        OutlinedTextField(
          value = minutesFieldValue,
          onValueChange = { newInput: TextFieldValue ->
            newInput.text.toLongOrNull()?.let {
              val str: String = newInput.text
              curMinutes = curMinutes.replaceTyping(str)

              if (curMinutes.length > 2) {
                curMinutes = curMinutes.substring(startIndex = 0, endIndex = 2)
              }
              curMinutes.toLongOrNull()?.let {
                val nit = max(min(it, 59L), 0L)
                curTime = curTime.div(60) * 60 + nit
                curMinutes = "${nit.div(10)}${nit.mod(10)}"
              }
              minutesFieldValue = newInput.copy(text = curMinutes)
              if (newInput.selection.start >= 2) {
                hoursFieldValue = hoursFieldValue.copy(selection = TextRange(0, 0))
                minutesFieldValue = minutesFieldValue.copy(selection = TextRange(0, 0))
                onConfirm(curTime)
                focusManager.clearFocus()
              }
            }
          },
          keyboardOptions = KeyboardOptions(
            showKeyboardOnFocus = true,
            keyboardType = KeyboardType.Number
          ),
          textStyle = TextStyle(
            fontSize = 31.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
          ),
          placeholder = { Text(text = curMinutes) },
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.Black
          ),
          modifier = Modifier
            .focusRequester(secondFocusRequester)
            .onFocusChanged { focusState: FocusState ->
              if (isFocused2 && !focusState.isFocused) {
                onConfirm(curTime)
                focusManager.clearFocus()
              }
              isFocused2 = focusState.isFocused
            }
            .wrapContentSize(unbounded = true)
            .height(100.dp)
            .width(100.dp)
        )
      }

      Text(
        text = "m",
        fontWeight = FontWeight.Bold,
        fontSize = 31.sp,
        modifier = Modifier
          .padding(
            start = 10.dp
          )
      )
    }
  }
}