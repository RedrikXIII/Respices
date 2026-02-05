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
  // currently selected screen
  // private modification, public access
  private val _currentScreen = mutableStateOf(Screen.START_PAGE)
  val currentScreen: State<Screen> get() = _currentScreen

  // currently selected meal (if selected)
  // private modification, public access
  private val _currentMeal = mutableStateOf<Meal?>(null)
  val currentMeal: State<Meal?> get() = _currentMeal

  // screen history
  private val _prevScreens: Stack<Screen> = Stack<Screen>()

  fun setCurrentScreen(newScreen: Screen) {
    if (_currentScreen.value == Screen.START_PAGE ||
        _currentScreen.value == Screen.MEAL_LIST ||
        _currentScreen.value == Screen.MEAL_VIEW ||
        _currentScreen.value == Screen.MEAL_EDIT) {
      // only saving some screens
      _prevScreens.push(_currentScreen.value)
    }

    // setting current screen to the new screen
    _currentScreen.value = newScreen

    if (newScreen == Screen.START_PAGE ||
        newScreen == Screen.MEAL_LIST) {
      // resetting the history on certain screens
      _prevScreens.clear()
    }
  }

  fun setCurrentMeal(newMeal: Meal?) {
    _currentMeal.value = newMeal
  }

  fun goToPrevScreen(): Boolean {
    if (_prevScreens.isEmpty())
      // reserve to default "go back" behaviour
      return false

    // setting the current screen to the top screen stored in the history
    _prevScreens.pop()?.let { popped ->
      _currentScreen.value = popped
    }

    return true
  }
}






