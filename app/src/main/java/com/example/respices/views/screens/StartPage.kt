package com.example.respices.views.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.R
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Meal
import com.example.respices.storage.entities.Tag
import com.example.respices.support.extensions.acceptanceIndex
import com.example.respices.support.extensions.isSelected
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.views.elements.HorizontalLine
import com.example.respices.views.elements.input.DurationPicker
import com.example.respices.views.elements.input.LocalSearchBar
import com.example.respices.views.elements.output.EditableList
import com.example.respices.views.elements.output.MealDisplay

@Composable
fun StartPage(
  bottomReached: Boolean,
  recipeViewModel: RecipeViewModel = viewModel()
) {
  RespicesTheme {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val allIngredientsState by recipeViewModel.allIngredients.collectAsStateWithLifecycle()
    val allIngredients: List<String> = allIngredientsState.map { i -> i.name }

    val allTagsState by recipeViewModel.allTags.collectAsStateWithLifecycle()
    val allTags: List<String> = allTagsState.map { i -> i.name }

    val allMealsState by recipeViewModel.allMeals.collectAsStateWithLifecycle()
    //Log.d("meals test", allMealsState.toString())
    val selectedIngredients = remember { mutableStateListOf<String>() }
    val selectedTags = remember { mutableStateListOf<String>() }

    val selectedTime = remember { mutableLongStateOf(1439) }

    val availableMeals by remember(
      allMealsState,
      selectedTime.longValue,
      selectedIngredients,
      selectedTags
    ) {
      derivedStateOf {
        allMealsState.filter { meal ->
            meal.isSelected(
              selectedTime.longValue,
              selectedIngredients.map { name ->
                Ingredient(0, name)
              }
            ) || (selectedIngredients.isEmpty() && selectedTags.isNotEmpty())
          }
      }
    }


    val availableMealsIndex = remember(availableMeals) {
      derivedStateOf {
        availableMeals.map { meal ->
          meal.acceptanceIndex(
            selectedTags.map { name ->
              Tag(0, name)
            }
          )
        }
      }
    }

    val suggestedMeals = remember { mutableStateListOf<Meal>() }

    LaunchedEffect(bottomReached) {
      if (bottomReached) {
        //Toast.makeText(context, "Available Meals: ${availableMeals.size}", Toast.LENGTH_SHORT).show()

//        Log.d("meals selection test", "time: ${selectedTime}")
//        Log.d("meals selection test", "ingredients: ${selectedIngredients}")
//        Log.d("meals selection test", "meals total: ${allMealsState.map {meal -> meal.toString2()}}")
//        Log.d("meals selection test", "meals selected: ${availableMeals.map {meal -> meal.toString2()}}")

        //Log.d("meals selection test", availableMealsIndex.value.toString())

        if (suggestedMeals.size < availableMeals.size) {
          var lowestSuggested: Double = 1000.0

          if (suggestedMeals.size > 0) {
            lowestSuggested = suggestedMeals[suggestedMeals.size - 1]
              .acceptanceIndex(
                selectedTags.map { name ->
                  Tag(0, name)
                }
              )
          }

          var highest: Double = -1000.0
          availableMealsIndex.value.forEachIndexed { index, value ->
            if (value > highest && value < lowestSuggested) {
              highest = value
            }
          }

          availableMeals.forEachIndexed { index, meal ->
            if (highest == availableMealsIndex.value[index]) {
              suggestedMeals.add(meal)
            }
          }
        }
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .pointerInput(Unit) {
          detectTapGestures(onTap = {
            focusManager.clearFocus()
          })
        }
    ) {
      LocalSearchBar(
        placeholder = "Ingredients...",
        options = allIngredients,
        onComplete = { str ->
          if (!selectedIngredients.contains(str) && str.isNotEmpty()) {
            selectedIngredients.add(str)
            suggestedMeals.clear()
          }
        },
        exclude = selectedIngredients
      )

      EditableList(selectedIngredients) { _ ->
        suggestedMeals.clear()
      }

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
            suggestedMeals.clear()
          }
        },
        exclude = selectedTags
      )

      EditableList(selectedTags) { _ ->
        suggestedMeals.clear()
      }

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
        initialTime = 0,
        onConfirm = { time ->
          suggestedMeals.clear()
          selectedTime.longValue = time
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

      if (availableMeals.isEmpty()) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp)
            .wrapContentHeight()
        ) {
          Image(
            painter = painterResource(R.drawable.serving_dish_empty),
            contentDescription = "empty meal list",
            modifier = Modifier
              .height(70.dp)
              .width(70.dp)
              .padding(bottom = 15.dp)
          )
          Text(
            text = "Oops, no meals available!",
            fontSize = 20.sp
          )
        }
      } else {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(min = 210.dp)
        ) {
          suggestedMeals.forEach { meal ->
            MealDisplay(meal)
          }

          if (suggestedMeals.size < availableMeals.size) {
            if (suggestedMeals.size == 0) {
              Spacer(
                modifier = Modifier
                  .height(40.dp)
              )
            }
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
  }
}