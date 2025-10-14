package com.example.respices.support.services

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.respices.support.enums.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object GlobalState {
  private val _currentScreen = mutableStateOf(Screen.START_PAGE)
  val currentScreen: State<Screen> get() = _currentScreen

  fun setCurrentScreen(newScreen: Screen) {
    _currentScreen.value = newScreen
  }
}