package com.example.respices.support.services

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.respices.storage.entities.Meal
import com.example.respices.support.enums.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object GlobalState {
  private val _currentScreen = mutableStateOf(Screen.START_PAGE)
  val currentScreen: State<Screen> get() = _currentScreen

  private val _currentMeal = mutableStateOf<Meal?>(null)
  val currentMeal: State<Meal?> get() = _currentMeal

  fun setCurrentScreen(newScreen: Screen) {
    _currentScreen.value = newScreen
  }

  fun setCurrentMeal(newMeal: Meal?) {
    _currentMeal.value = newMeal
  }
}