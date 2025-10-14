package com.example.respices.views.elements

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DurationPicker(
  initialTime: Long,
  onConfirm: (Long) -> Unit
) {
  var curTime by remember { mutableLongStateOf(initialTime) }

  var curHours by remember { mutableStateOf("")}
  var curMinutes by remember { mutableStateOf("")}

  val secondFocusRequester = remember { FocusRequester() }

  val focusManager = LocalFocusManager.current

  var isFocused by remember { mutableStateOf(false) }
  var isFocused2 by remember { mutableStateOf(false) }

  OutlinedTextField(
    value = curHours,
    onValueChange = { str ->
      curHours = str

      if (str.length >= 2) {
        val nstr = str.substring(startIndex = 0, endIndex = 2)
        curHours = nstr
        nstr.toLongOrNull()?.let {
          curTime = it*60 + curTime.mod(60)
        }
        secondFocusRequester.requestFocus()
      }
    },
    keyboardOptions = KeyboardOptions(
      showKeyboardOnFocus = true,
      keyboardType = KeyboardType.Number
    ),
    placeholder = { Text(text = curHours) },
    modifier = Modifier
      .onFocusChanged { focusState: FocusState ->
        if (isFocused && !focusState.isFocused) {
          onConfirm(curTime)
        }
        if (focusState.isFocused) {
          curHours = ""
        }
        isFocused = focusState.isFocused
      }
  )

  OutlinedTextField(
    value = curMinutes,
    onValueChange = { str ->
      curMinutes = str

      if (str.length >= 2) {
        val nstr = str.substring(startIndex = 0, endIndex = 2)
        curMinutes = nstr
        nstr.toLongOrNull()?.let {
          curTime = curTime.div(60)*60 + it
        }
        focusManager.clearFocus()
      }
    },
    keyboardOptions = KeyboardOptions(
      showKeyboardOnFocus = true,
      keyboardType = KeyboardType.Number
    ),
    placeholder = { Text(text = curMinutes)},
    modifier = Modifier
      .focusRequester(secondFocusRequester)
      .onFocusChanged { focusState: FocusState ->
        if (isFocused2 && !focusState.isFocused) {
          onConfirm(curTime)
        }
        if (focusState.isFocused) {
          curMinutes = ""
        }
        isFocused2 = focusState.isFocused
      }
  )
}