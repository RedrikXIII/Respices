package com.example.respices.views.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.R
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Meal
import com.example.respices.storage.entities.Tag
import com.example.respices.support.enums.MealFault
import com.example.respices.support.enums.Screen
import com.example.respices.support.extensions.getEmptyMeal
import com.example.respices.support.extensions.isEmpty
import com.example.respices.support.extensions.isValid
import com.example.respices.support.extensions.replaceTyping
import com.example.respices.support.extensions.toString2
import com.example.respices.support.services.GlobalState
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.views.elements.HorizontalLine
import com.example.respices.views.elements.input.DurationPicker
import com.example.respices.views.elements.input.LocalSearchBar
import com.example.respices.views.elements.output.EditableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealEdit(
  mealI: Meal?,
  recipeViewModel: RecipeViewModel = viewModel()
) {
  RespicesTheme {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val meal = remember { mutableStateOf<Meal>(getEmptyMeal()) }
    val initialMeal = remember { mutableStateOf<Meal>(getEmptyMeal()) }
    val isRewindConfirmed = remember { mutableStateOf(false) }
    val isNewMeal = remember { mutableStateOf<Boolean>(false) }
    val mealFault = remember { mutableStateOf(MealFault.NONE) }

    val allIngredientsState by recipeViewModel.allIngredients.collectAsStateWithLifecycle()
    val allIngredients: List<String> = allIngredientsState.map { i -> i.name }

    val allTagsState by recipeViewModel.allTags.collectAsStateWithLifecycle()
    val allTags: List<String> = allTagsState.map { i -> i.name }

    val mealIngredients = remember { mutableStateListOf<String>() }
    val mealTags = remember { mutableStateListOf<String>() }
    val mealTime = remember { mutableLongStateOf(0L) }

    var ratingFieldValue by remember { mutableStateOf(TextFieldValue()) }

    val isUpdateBlocked = remember { mutableStateOf(false) }

    val wasNameFocused = remember { mutableStateOf(false) }
    val wasRatingFocused = remember { mutableStateOf(false) }
    val wasStepsFocused = remember { mutableStateOf(false) }
    val wasLinkFocused = remember { mutableStateOf(false) }
    val wasRewindFocused = remember { mutableStateOf(false) }

    val rewindFocusRequester = remember { FocusRequester() }
    val rewindInteractionSource = remember { MutableInteractionSource() }

    val isChangesToast = remember { mutableStateOf(true) }

    val onMealChange = {
      meal.value = meal.value.copy(
        recipe = meal.value.recipe.copy(
          time = mealTime.longValue
        ),
        ingredients = mealIngredients.map { ing -> Ingredient(name = ing) },
        tags = mealTags.map { tag -> Tag(name = tag) }
      )

      val validation = meal.value.isValid()

      mealFault.value = validation.second

      Log.d(
        "meal edit test",
        "does edit pass? validation: ${validation.first}, update block: ${isUpdateBlocked.value}"
      )

      if (validation.first && !isUpdateBlocked.value) {
        if (isChangesToast.value) {
          Toast.makeText(context, "Changes saved!", Toast.LENGTH_SHORT).show()
        }
        isChangesToast.value = true

        // meal is valid - upsert
        isUpdateBlocked.value = true
        Log.d("meal edit test", "Valid change")

        scope.launch {
          delay(500L)
          isUpdateBlocked.value = false
        }

        recipeViewModel.upsertMeal(
          meal.value.recipe,
          meal.value.ingredients,
          meal.value.tags
        ) { index ->
          isUpdateBlocked.value = false
          Log.d("meal edit test", "Upserted successfully")
          recipeViewModel.allMeals.value.find { meal ->
            meal.recipe.recipeId == index
          }?.let { newMeal ->
            meal.value = newMeal

            mealIngredients.clear()
            mealIngredients.addAll(meal.value.ingredients.map { ing -> ing.name })

            mealTags.clear()
            mealTags.addAll(meal.value.tags.map { tag -> tag.name })

            mealTime.longValue = meal.value.recipe.time
            ratingFieldValue = ratingFieldValue.copy(
              text = (meal.value.recipe.rating * 10).div(10).toString()
            )

            GlobalState.setCurrentMeal(meal.value)
          }
        }
      }
    }

    LaunchedEffect(Unit) {
      if (mealI != null) {
        meal.value = mealI.copy()
        initialMeal.value = mealI.copy()

        mealIngredients.clear()
        mealIngredients.addAll(meal.value.ingredients.map { ing -> ing.name })

        mealTags.clear()
        mealTags.addAll(meal.value.tags.map { tag -> tag.name })

        mealTime.longValue = meal.value.recipe.time

        isNewMeal.value = false
      } else {
        isNewMeal.value = true
      }

      isRewindConfirmed.value = false
      isChangesToast.value = false
      onMealChange.invoke()
    }

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
      if (mealFault.value != MealFault.NONE && !meal.value.isEmpty()) {
        val errorMessage = when (mealFault.value) {
          MealFault.NO_NAME -> listOf("! Please specify the ", "name", " of the meal")
          MealFault.NO_TIME -> listOf("! Please specify a ", "non-zero time")
          MealFault.NO_INGREDIENTS -> listOf("! Please specify ", "at least 1", " ingredients")
          else -> listOf("")
        }

        Text(
          text = buildAnnotatedString {
            errorMessage.forEachIndexed { index, value ->
              if (index == 1) {
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                  append(value)
                }
              } else {
                append(value)
              }
            }
          },
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          color = Color(0xFFB22222),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
        )
      }

      if (!isNewMeal.value) {
        Box(
          modifier = Modifier
            .wrapContentSize()
            .onFocusChanged { focusState: FocusState ->
              Log.d(
                "meal edit rewind test",
                "focus changed from ${wasRewindFocused.value} to ${focusState.isFocused}"
              )
              if (!focusState.isFocused && wasRewindFocused.value) {
                isRewindConfirmed.value = false
                Log.d(
                  "meal edit rewind test",
                  "isRewindConfirmed reset: ${isRewindConfirmed.value}"
                )
              }

              wasRewindFocused.value = focusState.isFocused
            }
            .focusRequester(rewindFocusRequester)
            .focusable(true, rewindInteractionSource)
            .clickable(
              indication = null,
              interactionSource = rewindInteractionSource
            ) {
              scope.launch { rewindFocusRequester.requestFocus() }

              if (!isRewindConfirmed.value) {
                isRewindConfirmed.value = true
              } else {
                isRewindConfirmed.value = false

                if (isNewMeal.value) {
                  // delete new meal
                  recipeViewModel.deleteMeal(
                    recipe = meal.value.recipe
                  ) {
                    GlobalState.setCurrentScreen(Screen.MEAL_LIST)
                  }
                } else {
                  // revert the meal back to initial
                  meal.value = initialMeal.value.copy()

                  mealIngredients.clear()
                  mealIngredients.addAll(
                    initialMeal.value.ingredients.map { ing -> ing.name }
                  )

                  mealTags.clear()
                  mealTags.addAll(
                    initialMeal.value.tags.map { tag -> tag.name }
                  )

                  mealTime.longValue = initialMeal.value.recipe.time

                  isChangesToast.value = false
                  Toast.makeText(context, "Rewound successfully!", Toast.LENGTH_SHORT).show()
                  onMealChange.invoke()
                }
              }

            }
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
              .wrapContentWidth()
              .wrapContentHeight()
              .padding(top = 20.dp)
              .background(
                color = MaterialTheme.colorScheme.onTertiary,
                shape = MaterialTheme.shapes.large
              )
              .border(
                color = Color.Black,
                width = 1.dp,
                shape = MaterialTheme.shapes.large
              )
          ) {
            Image(
              painter = painterResource(R.drawable.circular_arrow),
              contentDescription = "rewind icon",
              modifier = Modifier
                .padding(all = 20.dp)
                .height(30.dp)
                .aspectRatio(1.0f)
            )

            if (!isRewindConfirmed.value) {
              Text(
                text = "Rewind",
                fontSize = 22.sp,
                modifier = Modifier
                  .padding(vertical = 20.dp)
                  .padding(end = 20.dp)
              )
            } else {
              Text(
                text = "Are you sure?",
                fontSize = 22.sp,
                modifier = Modifier
                  .padding(vertical = 20.dp)
                  .padding(end = 20.dp)
              )
            }
          }
        }
      }

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .width(300.dp)
          .wrapContentHeight()
          .padding(top = 40.dp, bottom = 10.dp)
      ) {
        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )

        Text(
          text = "Name",
          fontSize = 28.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 10.dp)
        )

        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )
      }

      OutlinedTextField(
        value = meal.value.recipe.name,
        onValueChange = { text ->
          meal.value = meal.value.copy(
            recipe = meal.value.recipe.copy(name = text)
          )
        },
        keyboardOptions = KeyboardOptions(
          showKeyboardOnFocus = true,
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(
          fontSize = 26.sp,
          textAlign = TextAlign.Start,
        ),
        placeholder = { Text(text = "...") },
        colors = OutlinedTextFieldDefaults.colors(
          cursorColor = Color.Black,
          focusedBorderColor = Color.Black,
          unfocusedBorderColor = Color.Black,
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          selectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.onSurface,
            backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
          )
        ),
        modifier = Modifier
          .bringIntoViewRequester(bringIntoViewRequester)
          .fillMaxWidth()
          .background(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.extraSmall
          )
          .onFocusChanged { focusState ->
            if (!focusState.isFocused && wasNameFocused.value) {
              onMealChange.invoke()
            }

            wasNameFocused.value = focusState.isFocused
          }
      )

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .width(300.dp)
          .wrapContentHeight()
          .padding(top = 40.dp, bottom = 10.dp)
      ) {
        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )

        Text(
          text = "Ingredients",
          fontSize = 28.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 10.dp)
        )

        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )
      }

      LocalSearchBar(
        placeholder = "Ingredients...",
        options = allIngredients,
        onComplete = { str ->
          if (!mealIngredients.contains(str.lowercase()) && str.isNotEmpty()) {
            mealIngredients.add(str.lowercase())
            onMealChange.invoke()
          }
        },
        exclude = mealIngredients
      )

      EditableList(mealIngredients, arrangement = Arrangement.Center) { _ ->
        onMealChange.invoke()
      }

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .width(300.dp)
          .wrapContentHeight()
          .padding(top = 40.dp, bottom = 10.dp)
      ) {
        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )

        Text(
          text = "Tags",
          fontSize = 28.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 10.dp)
        )

        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )
      }

      LocalSearchBar(
        placeholder = "Tags...",
        options = allTags,
        onComplete = { str ->
          if (!mealTags.contains(str.lowercase()) && str.isNotEmpty()) {
            mealTags.add(str.lowercase())
            onMealChange.invoke()
          }
        },
        exclude = mealTags
      )

      EditableList(mealTags, arrangement = Arrangement.Center) { _ ->
        onMealChange.invoke()
      }

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .width(300.dp)
          .wrapContentHeight()
          .padding(top = 40.dp, bottom = 10.dp)
      ) {
        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )

        Text(
          text = "Time",
          fontSize = 28.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 10.dp)
        )

        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )
      }

      DurationPicker(
        initialTime = mealTime.longValue,
        onConfirm = { time ->
          mealTime.longValue = time

          onMealChange.invoke()
        }
      )

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .width(300.dp)
          .wrapContentHeight()
          .padding(top = 40.dp, bottom = 10.dp)
      ) {
        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )

        Text(
          text = "Rating",
          fontSize = 28.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 10.dp)
        )

        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
          .wrapContentHeight()
          .fillMaxWidth()
          .padding(top = 20.dp, bottom = 10.dp)
      ) {
        Image(
          painter = painterResource(R.drawable.outline_kid_star_24),
          contentDescription = "star icon",
          modifier = Modifier
            .padding(end = 20.dp)
            .height(60.dp)
            .aspectRatio(1.0f)
        )

        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .height(60.dp)
            .width(80.dp)
            .graphicsLayer { clip = true }
            .background(
              color = MaterialTheme.colorScheme.surface
            )
            .border(
              color = Color.Black,
              width = 1.dp
            )
            .padding(all = 10.dp)
        ) {
          OutlinedTextField(
            value = ratingFieldValue,
            onValueChange = { newInputPre: TextFieldValue ->
              var newText = ratingFieldValue.text.replaceTyping(
                newInputPre.text,
                newInputPre.selection.start
              )
              if (newText == "") {
                newText = "0"
              }
              if (newText.contains('.')) {
                newText += "0"
              }
              Log.d("rating edit test", "rating input: $newText")
              var newInput = newInputPre.copy(text = newText)

              newInput.text.toDoubleOrNull()?.let { newRPre ->
                var newR = max(min(newRPre, 10.0), 0.0)
                newR = (newR * 10).div(10)

                if (meal.value.recipe.rating >= 10.0 && newR < 10.0) {
                  newInput = newInput.copy(
                    selection = TextRange(newInput.selection.start - 1)
                  )
                }

                ratingFieldValue = ratingFieldValue.copy(
                  text = "$newR",
                  selection = newInput.selection
                )
                meal.value = meal.value.copy(
                  recipe = meal.value.recipe.copy(rating = newR)
                )
              }
            },
            keyboardOptions = KeyboardOptions(
              showKeyboardOnFocus = true,
              keyboardType = KeyboardType.Decimal,
              imeAction = ImeAction.Done
            ),
            textStyle = TextStyle(
              fontSize = 31.sp,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center,
            ),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              cursorColor = Color.Black,
              focusedBorderColor = Color.Black,
              unfocusedBorderColor = Color.Black,
              focusedTextColor = Color.Black,
              unfocusedTextColor = Color.Black,
              selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.onSurface,
                backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
              )
            ),
            modifier = Modifier
              .bringIntoViewRequester(bringIntoViewRequester)
              .onFocusChanged { focusState: FocusState ->
                if (!focusState.isFocused && wasRatingFocused.value) {
                  onMealChange.invoke()
                }

                if (focusState.isFocused && !wasRatingFocused.value) {
                  coroutineScope.launch {
                    delay(50)
                    ratingFieldValue = ratingFieldValue.copy(
                      selection = TextRange(0)
                    )
                  }
                }

                wasRatingFocused.value = focusState.isFocused
              }
              .wrapContentSize(unbounded = true)
              .width(100.dp)
              .height(100.dp)
          )
        }

        Text(
          text = "/10",
          fontWeight = FontWeight.Bold,
          fontSize = 31.sp,
          modifier = Modifier
            .padding(
              start = 10.dp,
              end = 15.dp
            )
        )
      }

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .width(300.dp)
          .wrapContentHeight()
          .padding(top = 40.dp, bottom = 10.dp)
      ) {
        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )

        Text(
          text = "Steps",
          fontSize = 28.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 10.dp)
        )

        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )
      }

      OutlinedTextField(
        value = meal.value.recipe.steps,
        onValueChange = { text ->
          meal.value = meal.value.copy(
            recipe = meal.value.recipe.copy(steps = text)
          )
        },
        keyboardOptions = KeyboardOptions(
          showKeyboardOnFocus = true,
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(
          fontSize = 22.sp,
          textAlign = TextAlign.Start,
        ),
        placeholder = { Text(
          text = "...",
          modifier = Modifier
            .fillMaxWidth()
        ) },
        colors = OutlinedTextFieldDefaults.colors(
          cursorColor = Color.Black,
          focusedBorderColor = Color.Black,
          unfocusedBorderColor = Color.Black,
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          selectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.onSurface,
            backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
          )
        ),
        modifier = Modifier
          .bringIntoViewRequester(bringIntoViewRequester)
          .fillMaxWidth()
          .background(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.extraSmall
          )
          .onFocusChanged { focusState ->
            if (!focusState.isFocused && wasStepsFocused.value) {
              onMealChange.invoke()
            }

            wasStepsFocused.value = focusState.isFocused
          }
      )

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .width(300.dp)
          .wrapContentHeight()
          .padding(top = 40.dp, bottom = 10.dp)
      ) {
        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )

        Text(
          text = "Link",
          fontSize = 28.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 10.dp)
        )

        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )
      }

      OutlinedTextField(
        value = meal.value.recipe.link,
        onValueChange = { text ->
          meal.value = meal.value.copy(
            recipe = meal.value.recipe.copy(link = text)
          )
        },
        keyboardOptions = KeyboardOptions(
          showKeyboardOnFocus = true,
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(
          fontSize = 22.sp,
          textAlign = TextAlign.Center,
          color = Color.Blue,
          textDecoration = TextDecoration.Underline
        ),
        placeholder = { Text(
          text = "...",
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
        ) },
        colors = OutlinedTextFieldDefaults.colors(
          cursorColor = Color.Black,
          focusedBorderColor = Color.Black,
          unfocusedBorderColor = Color.Black,
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          selectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.onSurface,
            backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
          )
        ),
        modifier = Modifier
          .bringIntoViewRequester(bringIntoViewRequester)
          .fillMaxWidth()
          .background(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.extraSmall
          )
          .onFocusChanged { focusState ->
            if (!focusState.isFocused && wasLinkFocused.value) {
              onMealChange.invoke()
            }

            wasLinkFocused.value = focusState.isFocused
          }
      )

      Spacer(
        modifier = Modifier
          .height(70.dp)
      )
    }
  }
}