package com.example.respices.views.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.R
import com.example.respices.support.services.SearchBarManager
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.views.elements.DurationPicker
import com.example.respices.views.elements.EditableList
import com.example.respices.views.elements.LocalSearchBar

@Composable
fun StartPage(
  recipeViewModel: RecipeViewModel = viewModel()
) {
  RespicesTheme {
    val allIngredientsState by recipeViewModel.allIngredients.collectAsStateWithLifecycle()
    val allIngredients: List<String> = allIngredientsState.map { i -> i.name }

    val allTagsState by recipeViewModel.allTags.collectAsStateWithLifecycle()
    val allTags: List<String> = allTagsState.map { i -> i.name }

    var selectedIngredients = remember { mutableStateListOf<String>() }
    var selectedTags = remember { mutableStateListOf<String>() }

    Column(

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

      DurationPicker(
        initialTime = 0L,
        onConfirm = { time ->
          Log.d("time select test", "${time.div(60L)}:${time.mod(60L)}")
        }
      )

      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .height(2.dp)
      )


    }
  }
}