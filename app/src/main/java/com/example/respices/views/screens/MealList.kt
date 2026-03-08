package com.example.respices.views.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.R
import com.example.respices.storage.entities.Meal
import com.example.respices.storage.entities.Tag
import com.example.respices.support.extensions.acceptanceIndex
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.views.elements.output.MealDisplay

// UI Screen to view a dynamic list of all meals
@Composable
fun MealList(
  bottomReached: Boolean,
  recipeViewModel: RecipeViewModel = viewModel()
) {
  RespicesTheme {
    val context = LocalContext.current
    val allMealsState by recipeViewModel.allMeals.collectAsStateWithLifecycle()
    val loadedMeals = remember { mutableStateListOf<Meal>() }

    LaunchedEffect(bottomReached) {
      if (bottomReached) {
        if (loadedMeals.size < allMealsState.size) {
          // Load a new meal once bottom reached and meals left to load
          // Meals are shown in alphabetical order of their names

          val allMealNames = allMealsState.map { meal -> meal.recipe.name }

          var highestLoaded: String = ""

          // Recording latest in the alphabet name of the meal loaded
          if (loadedMeals.size > 0) {
            highestLoaded = loadedMeals[loadedMeals.size - 1].recipe.name
          }

          // Selection Sort - selecting next meal to laod
          var lowest: String = "\uFFFF"
          allMealNames.forEach { value ->
            if (value < lowest && value > highestLoaded) {
              lowest = value
            }
          }

          allMealsState.forEachIndexed { index, meal ->
            if (lowest == allMealNames[index]) {
              loadedMeals.add(meal)
            }
          }
        }
      }
    }

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(bottom = 20.dp)
    ) {
      if (allMealsState.isEmpty()) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
            .wrapContentHeight()
        ) {
          Image(
            painter = painterResource(R.drawable.serving_dish_empty),
            contentDescription = "empty meal list",
            modifier = Modifier
              .height(120.dp)
              .width(120.dp)
              .padding(bottom = 30.dp)
          )
          Text(
            text = "Oops, no meals!",
            fontSize = 30.sp
          )
        }
      }

      // Display all loaded meals as MealDisplay
      loadedMeals.forEach {meal ->
        MealDisplay(meal)
      }

      if (loadedMeals.size < allMealsState.size) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
            .height(60.dp)
        ) {
          Image(
            painter = painterResource(R.drawable.outline_keyboard_arrow_down_24),
            contentDescription = "scroll down",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
              .fillMaxHeight()
              .aspectRatio(1.5f)
          )
        }
      }
    }
  }
}

