package com.example.respices.views.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.R
import com.example.respices.storage.entities.Meal
import com.example.respices.support.enums.Screen
import com.example.respices.support.extensions.getEmptyMeal
import com.example.respices.support.services.GlobalState
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.views.elements.HorizontalLine
import kotlinx.coroutines.launch

@Composable
fun MealDelete(
  mealI: Meal?,
  recipeViewModel: RecipeViewModel = viewModel()
) {
  RespicesTheme {
    val copyMeal = remember { mutableStateOf(getEmptyMeal()) }
    val isDeleted = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
      if (mealI == null && !isDeleted.value) {
        GlobalState.setCurrentScreen(Screen.MEAL_LIST)
      }

      mealI?.let { mealI2 ->
        copyMeal.value = mealI2.copy()
      }
    }

    if (!isDeleted.value) {
      Text(
        text = "Deleting...",
        fontSize = 34.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 60.dp, bottom = 30.dp)
      )

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(top = 30.dp)
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
          text = copyMeal.value.recipe.name,
          fontSize = 28.sp,
          style = TextStyle(
            lineHeight = 34.sp
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .widthIn(max = 300.dp)
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

      Text(
        text = "Are you sure?",
        fontSize = 28.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 60.dp)
      )

      Text(
        text = buildAnnotatedString {
          append("Once you leave the screen, the deletion will be ")
          withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("irreversible")
          }
          append(" and ")
          withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("permanent")
          }
        },
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color(0xFFB22222),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 20.dp)
      )

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
          ) {
            isDeleted.value = true
            recipeViewModel.deleteMeal(copyMeal.value.recipe)
            GlobalState.setCurrentMeal(null)
          }
          .padding(top = 40.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(top = 20.dp)
            .border(
              color = Color.Black,
              width = 1.dp,
              shape = MaterialTheme.shapes.large
            )
            .align(Alignment.Center)
        ) {
          Image(
            painter = painterResource(R.drawable.outline_delete_24),
            contentDescription = "rewind icon",
            modifier = Modifier
              .padding(all = 20.dp)
              .height(40.dp)
              .aspectRatio(1.0f)
          )

          Text(
            text = "Delete",
            fontSize = 26.sp,
            modifier = Modifier
              .padding(vertical = 20.dp)
              .padding(end = 20.dp)
          )
        }
      }
    }

    if (isDeleted.value) {
      Text(
        text = "Deleted",
        fontSize = 34.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 60.dp, bottom = 30.dp)
      )

      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(top = 30.dp)
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
          text = copyMeal.value.recipe.name,
          fontSize = 28.sp,
          style = TextStyle(
            lineHeight = 34.sp
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .widthIn(max = 300.dp)
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

      Text(
        text = buildAnnotatedString {
          append("While on this screen, you can still ")
          withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("revert")
          }
          append(" the deletion")
        },
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color(0xFFB22222),
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 60.dp)
      )

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
          ) {
            isDeleted.value = false
            recipeViewModel.upsertMeal(
              copyMeal.value.recipe,
              copyMeal.value.ingredients,
              copyMeal.value.tags
            )
            GlobalState.setCurrentMeal(copyMeal.value.copy())
          }
          .padding(top = 40.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(top = 20.dp)
            .border(
              color = Color.Black,
              width = 1.dp,
              shape = MaterialTheme.shapes.large
            )
            .align(Alignment.Center)
        ) {
          Image(
            painter = painterResource(R.drawable.circular_arrow),
            contentDescription = "rewind icon",
            modifier = Modifier
              .padding(all = 20.dp)
              .height(40.dp)
              .aspectRatio(1.0f)
          )

          Text(
            text = "Revert",
            fontSize = 26.sp,
            modifier = Modifier
              .padding(vertical = 20.dp)
              .padding(end = 20.dp)
          )
        }
      }
    }
  }
}