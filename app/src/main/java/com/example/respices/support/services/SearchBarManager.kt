package com.example.respices.support.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SearchBarManager {
  var curInput = mutableStateOf<String>("")

  var curOptions by mutableStateOf<List<String>>(listOf())
    private set

  var curPlaceholder by mutableStateOf<String>("")
    private set

  var isActive by mutableStateOf(false)
    private set

  private var onComplete: (String) -> Unit = {}

  fun launchSearchBar(
    options: List<String> = listOf(),
    placeholder: String = "",
    onDone: (String) -> Unit
  ) {
    if (isActive)
      closeSearchBar()

    curInput.value = ""
    curOptions = options
    curPlaceholder = placeholder
    isActive = true
    onComplete = onDone
  }

  fun closeSearchBar() {
    curOptions = listOf()
    curPlaceholder = ""
    isActive = false
    onComplete.invoke(curInput.value)
    onComplete = { _ -> }
  }
}