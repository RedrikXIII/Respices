package com.example.respices.viewmodel

import androidx.compose.runtime.compositionLocalOf

// Create a local instance of RecipeViewModel
val LocalRecipeViewModel = compositionLocalOf<RecipeViewModel> {
  error("No VM")
}