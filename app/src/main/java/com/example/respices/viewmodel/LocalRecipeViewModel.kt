package com.example.respices.viewmodel

import androidx.compose.runtime.compositionLocalOf

val LocalRecipeViewModel = compositionLocalOf<RecipeViewModel> {
  error("No VM")
}