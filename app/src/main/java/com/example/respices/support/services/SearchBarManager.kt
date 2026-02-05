package com.example.respices.support.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SearchBarManager {
  // current Search Bar input
  var curInput = mutableStateOf<String>("")

  // current Search Bar options it selects suggestions from
  var curOptions by mutableStateOf<List<String>>(listOf())
    private set

  // Search Bar placeholder in case nothing is typed
  var curPlaceholder by mutableStateOf<String>("")
    private set

  // whether Search Bar is open or closed
  var isActive by mutableStateOf(false)
    private set

  // is invoked with the final result of the search input
  private var onComplete: (String) -> Unit = {}

  var excludeOptions by mutableStateOf<List<String>>(listOf())
    private set

  // invoke Global Search Bar with specific parameters
  fun launchSearchBar(
    options: List<String> = listOf(),
    exclude: List<String> = listOf(),
    placeholder: String = "",
    onDone: (String) -> Unit
  ) {
    if (isActive)
      closeSearchBar()

    curInput.value = ""
    curOptions = options
    excludeOptions = exclude
    curPlaceholder = placeholder
    isActive = true
    onComplete = onDone
  }

  // close the Global Search Bar and return the result
  fun closeSearchBar() {
    curOptions = listOf()
    curPlaceholder = ""
    isActive = false
    onComplete.invoke(curInput.value)
    onComplete = { _ -> }
  }
}