package com.example.respices.views.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.views.elements.input.DurationPicker
import com.example.respices.views.elements.output.EditableList
import com.example.respices.views.elements.HorizontalLine
import com.example.respices.views.elements.input.LocalSearchBar
import com.example.respices.views.elements.output.MealDisplay

@Composable
fun StartPage(
  recipeViewModel: RecipeViewModel = viewModel()
) {
  RespicesTheme {
    val allIngredientsState by recipeViewModel.allIngredients.collectAsStateWithLifecycle()
    val allIngredients: List<String> = allIngredientsState.map { i -> i.name }

    val allTagsState by recipeViewModel.allTags.collectAsStateWithLifecycle()
    val allTags: List<String> = allTagsState.map { i -> i.name }

    val allMealsState by recipeViewModel.allMeals.collectAsStateWithLifecycle()
    Log.d("meals test", allMealsState.toString())
    var selectedIngredients = remember { mutableStateListOf<String>() }
    var selectedTags = remember { mutableStateListOf<String>() }

    Column(
      modifier = Modifier
        .fillMaxWidth()
    ) {
      LocalSearchBar(
        placeholder = "Ingredients...",
        options = allIngredients,
        onComplete = { str ->
          if (!selectedIngredients.contains(str) && str.isNotEmpty()) {
            selectedIngredients.add(str)
          }
        }
      )

      EditableList(selectedIngredients)

      HorizontalLine(
        color = Color.Black,
        lineModifier = Modifier
          .width(300.dp)
          .height(1.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(40.dp)
      )

      LocalSearchBar(
        placeholder = "Tags...",
        options = allTags,
        onComplete = { str ->
          if (!selectedTags.contains(str) && str.isNotEmpty()) {
            selectedTags.add(str)
          }
        }
      )

      EditableList(selectedTags)

      HorizontalLine(
        color = Color.Black,
        lineModifier = Modifier
          .width(300.dp)
          .height(1.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(40.dp)
      )

      DurationPicker(
        initialTime = 0L,
        onConfirm = { time ->
          Log.d("time select test", "${time.div(60L)}:${time.mod(60L)}")
        }
      )

      HorizontalLine(
        color = Color.Black,
        lineModifier = Modifier
          .fillMaxWidth()
          .height(2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
      )

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
      ) {
        allMealsState.forEach {meal ->
          MealDisplay(meal)
        }
      }
    }
  }
}