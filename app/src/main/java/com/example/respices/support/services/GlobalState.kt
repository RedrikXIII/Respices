package com.example.respices.support.services

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.respices.storage.entities.Meal
import com.example.respices.support.enums.Screen
import com.example.respices.support.utility.Stack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object GlobalState {
  private val _currentScreen = mutableStateOf(Screen.START_PAGE)
  val currentScreen: State<Screen> get() = _currentScreen

  private val _prevScreens: Stack<Screen> = Stack<Screen>()

  private val _currentMeal = mutableStateOf<Meal?>(null)
  val currentMeal: State<Meal?> get() = _currentMeal

  fun setCurrentScreen(newScreen: Screen) {
    if (_currentScreen.value == Screen.START_PAGE ||
        _currentScreen.value == Screen.MEAL_LIST ||
        _currentScreen.value == Screen.MEAL_VIEW ||
        _currentScreen.value == Screen.MEAL_EDIT) {
      _prevScreens.push(_currentScreen.value)
      Log.d("go back test", "pushed to stack: $_prevScreens")
    }

    _currentScreen.value = newScreen

    if (newScreen == Screen.START_PAGE ||
        newScreen == Screen.MEAL_LIST) {
      _prevScreens.clear()
      Log.d("go back test", "cleared stack: $_prevScreens")
    }
  }

  fun setCurrentMeal(newMeal: Meal?) {
    _currentMeal.value = newMeal
  }

  fun goToPrevScreen(): Boolean {
    Log.d("go back test", "go to prev with: $_prevScreens")

    if (_prevScreens.isEmpty())
      return false

    _prevScreens.pop()?.let { popped ->
      _currentScreen.value = popped
    }

    return true
  }
}