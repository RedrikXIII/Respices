package com.example.respices.views.elements.input

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@Composable
fun DurationPicker(
  initialTime: Long,
  onConfirm: (Long) -> Unit
) {
  var curTime by remember { mutableLongStateOf(initialTime) }

  val ih = max(min(curTime.div(60L), 23L), 0L).toString()
  val im0 = max(min(curTime.mod(60L), 59L), 0L)
  val im = "${im0.div(10L)}${im0.mod(10L)}"

  var hoursFieldValue by remember { mutableStateOf(TextFieldValue(ih)) }
  var minutesFieldValue by remember { mutableStateOf(TextFieldValue(im)) }

  var curHours by remember { mutableStateOf(ih) }
  var curMinutes by remember { mutableStateOf(im) }

  val focusManager = LocalFocusManager.current

  val bringIntoViewRequester = remember { BringIntoViewRequester() }
  val coroutineScope = rememberCoroutineScope()

  var wasFocused by remember { mutableStateOf(false) }
  var wasFocused2 by remember { mutableStateOf(false) }

  var selectionPrev by remember { mutableStateOf(TextRange(0, 0)) }

  val hourFocusRequester = remember { FocusRequester() }
  val minuteFocusRequester = remember { FocusRequester() }

  var isMinuteTransition by remember { mutableStateOf(false) }
  var isFocusClearRequested by remember { mutableStateOf(false) }

  LaunchedEffect(isFocusClearRequested) {
    if (isFocusClearRequested) {
      focusManager.clearFocus(force = true)
      isFocusClearRequested = false

      if (isMinuteTransition) {
        minuteFocusRequester.requestFocus()
        isMinuteTransition = false
      }
    }
  }

  LaunchedEffect(initialTime) {
    curTime = initialTime

    val ihi = max(min(curTime.div(60L), 23L), 0L).toString()
    val im0i = max(min(curTime.mod(60L), 59L), 0L)
    val imi = "${im0i.div(10L)}${im0i.mod(10L)}"

    curHours = ihi
    curMinutes = imi

    hoursFieldValue = hoursFieldValue.copy(text = ihi)
    minutesFieldValue = minutesFieldValue.copy(text = imi)
  }

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
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
          ) {
            hourFocusRequester.requestFocus()
          }
      ) {
        OutlinedTextField(
          value = hoursFieldValue,
          onValueChange = { newInputPre: TextFieldValue ->
            var newText = newInputPre.text
            if (newText == "") {
              newText = "0"
            }
            var newInput = newInputPre.copy(text = newText)

            newInput.text.toLongOrNull()?.let { newInputLong ->
              val str: String = newInput.text
              curHours = curHours.replaceTyping(str, newInput.selection.end)

              if (curHours.length > 2) {
                curHours = curHours.substring(startIndex = 0, endIndex = 2)
              }

              curHours.toLongOrNull()?.let { curHoursLong ->
                Log.d("time picker test", "new: $curHoursLong, old: ${curTime.div(60L)}")
                if (curHoursLong < 10L && curTime.div(60L) >= 10L) {
                  newInput = newInput.copy(
                    selection = TextRange(newInput.selection.start - 1)
                  )
                  Log.d("time picker test", "selection changed: ${newInput.selection}")
                }

                val nit = max(min(curHoursLong, 23L), 0L)
                curTime = nit * 60 + curTime.mod(60)
                curHours = nit.toString()
              }
              hoursFieldValue = newInput.copy(text = curHours)

              Log.d("time picker test", "copied selection: ${hoursFieldValue.selection}")

              if (newInput.selection.length == 0 &&
                  selectionPrev.length == 0 && (
                    (newInput.selection.end >= 2 &&
                     newInput.selection.end - selectionPrev.end == 1) ||
                    (newInput.selection.end == 1 &&
                     curTime.div(60L) == 0L
                    ))
              ) {
                onConfirm(curTime)
                isMinuteTransition = true
                isFocusClearRequested = true
              }
            }

            selectionPrev = newInput.selection
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
            .bringIntoViewRequester(bringIntoViewRequester)
            .focusRequester(hourFocusRequester)
            .onFocusChanged { focusState: FocusState ->
              if (!focusState.isFocused && wasFocused) {
                onConfirm.invoke(curTime)
              }
              if (focusState.isFocused && !wasFocused) {
                coroutineScope.launch {
                  delay(50)
                  selectionPrev = TextRange(0)
                  hoursFieldValue = hoursFieldValue.copy(selection = TextRange(0))
                }
              }
              if (focusState.isFocused) {
                coroutineScope.launch {
                  bringIntoViewRequester.bringIntoView()
                }
              }

              wasFocused = focusState.isFocused
            }
            .wrapContentSize(unbounded = true)
            .width(100.dp)
            .height(100.dp)
        )
      }

      Text(
        text = "h",
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
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
          ) {
            //minuteFocusRequester.requestFocus()
          }
      ) {
        OutlinedTextField(
          value = minutesFieldValue,
          onValueChange = { newInputPre: TextFieldValue ->
            var newText = newInputPre.text
            if (newText == "") {
              newText = "0"
            }
            val newInput = newInputPre.copy(text = newText)

            newInput.text.toLongOrNull()?.let {
              val str: String = newInput.text
              curMinutes = curMinutes.replaceTyping(str, newInput.selection.end)

              if (curMinutes.length > 2) {
                curMinutes = curMinutes.substring(startIndex = 0, endIndex = 2)
              }
              curMinutes.toLongOrNull()?.let {
                val nit = max(min(it, 59L), 0L)
                curTime = curTime.div(60) * 60 + nit
                curMinutes = "${nit.div(10)}${nit.mod(10)}"
              }
              minutesFieldValue = newInput.copy(text = curMinutes)

              if (newInput.selection.length == 0 &&
                  newInput.selection.end >= 2 &&
                  selectionPrev.length == 0 &&
                  newInput.selection.end - selectionPrev.end == 1
              ) {
                onConfirm(curTime)
                isFocusClearRequested = true
              }
            }

            selectionPrev = newInput.selection
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
            .bringIntoViewRequester(bringIntoViewRequester)
            .focusRequester(minuteFocusRequester)
            .onFocusChanged { focusState: FocusState ->
              if (!focusState.isFocused && wasFocused2) {
                onConfirm(curTime)
              }
              if (focusState.isFocused && !wasFocused2) {
                coroutineScope.launch {
                  delay(50)
                  selectionPrev = TextRange(0)
                  minutesFieldValue = minutesFieldValue.copy(selection = TextRange(0))
                }
              }
              if (focusState.isFocused) {
                coroutineScope.launch {
                  bringIntoViewRequester.bringIntoView()
                }
              }

              wasFocused2 = focusState.isFocused
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