package com.example.respices.views.screens

import android.content.ClipData
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.R
import com.example.respices.support.classes.MealData
import com.example.respices.support.extensions.isValidMealDataList
import com.example.respices.support.extensions.toMeal
import com.example.respices.support.extensions.toMealData
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.views.elements.input.IconButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Composable
fun FileView(recipeViewModel: RecipeViewModel = viewModel()) {
  RespicesTheme {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val clipboard: Clipboard = LocalClipboard.current

    val initialFile = remember { mutableStateOf("") }
    val currentFile = remember { mutableStateOf("") }

    val allMealsState by recipeViewModel.allMeals.collectAsStateWithLifecycle()
    val allMealsJson by remember(
      allMealsState
    ) {
      derivedStateOf {
        allMealsState.map { meal ->
          Json.encodeToString(MealData.serializer(), meal.toMealData())
        }.joinToString(
          separator = ",\n  ",
          prefix = "[\n  ",
          postfix = "\n]"
        )
      }
    }

    val isFileValid = remember { mutableStateOf(true) }

    val isChangesSaved = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
      initialFile.value = allMealsJson
      currentFile.value = initialFile.value
    }

    LaunchedEffect(currentFile.value) {
      if (currentFile.value.isValidMealDataList()) {
        isFileValid.value = true

//        Log.d("file view test", "valid new file")

        if (allMealsJson != currentFile.value) {
          if (isChangesSaved.value) {
            Toast.makeText(context, "Changes saved!", Toast.LENGTH_SHORT).show()
          }
          isChangesSaved.value = true

          val allNewMealData = Json.decodeFromString(
            ListSerializer(MealData.serializer()),
            currentFile.value
          )
          val allNewMeals = allNewMealData.map { mealData ->
            mealData.toMeal()
          }

          recipeViewModel.clearAll {
            scope.launch {
              allNewMeals.forEach { meal ->
                recipeViewModel.upsertMeal(
                  recipe = meal.recipe,
                  ingredients = meal.ingredients,
                  tags = meal.tags
                )

                delay(50)
              }

              recipeViewModel.loadAllMeals { }
            }
          }
        }
      } else {
        isFileValid.value = false

//        Log.d("file view test", "invalid new file")
      }

//      Log.d("file view test", "new file: ${currentFile.value}")
    }

    Text(
      text = "Meals List File",
      fontSize = 32.sp,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 30.dp)
    )

    if (!isFileValid.value) {
      Text(
        text = buildAnnotatedString {
          append("Invalid meal list. Changes were ")
          withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("not saved")
          }
          append(". Paste in a ")
          withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("valid meal list")
          }
        },
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color(0xFFB22222),
        modifier = Modifier
          .fillMaxWidth()
          .height(70.dp)
      )
    } else {
      Spacer(
        modifier = Modifier
          .height(70.dp)
      )
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
        .border(
          color = Color.Black,
          width = 1.dp
        )
        .padding(10.dp)
        .verticalScroll(rememberScrollState())
        .horizontalScroll(rememberScrollState())
    ) {
      Text(
        text = currentFile.value,
        fontSize = 18.sp
      )
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 40.dp)
        .padding(horizontal = 10.dp)
        .height(90.dp)
    ) {
      IconButton(
        R.drawable.copy,
        modifier = Modifier
          .padding(horizontal = 10.dp)
          .clickable {
            scope.launch {
              clipboard.setClipEntry(
                ClipEntry(
                  ClipData.newPlainText(
                    "meals file",
                    currentFile.value
                  )
                )
              )
            }
          },
        imageModifier = Modifier
          .padding(20.dp)
      )

      IconButton(
        R.drawable.paste,
        modifier = Modifier
          .padding(horizontal = 10.dp)
          .clickable {
            scope.launch {
              var res = ""
              clipboard.getClipEntry()?.clipData?.let { data ->
                if (data.itemCount > 0) {
                  res = data.getItemAt(0)
                    .coerceToText(context)
                    .toString()
                }
              }

              currentFile.value = res
            }
          },
        imageModifier = Modifier
          .padding(20.dp)
      )

      IconButton(
        R.drawable.circular_arrow,
        modifier = Modifier
          .padding(horizontal = 10.dp)
          .clickable {
            Toast.makeText(context, "Rewound successfully!", Toast.LENGTH_SHORT).show()
            isChangesSaved.value = false

            currentFile.value = initialFile.value
          },
        imageModifier = Modifier
          .padding(20.dp)
      )
    }

//    SelectionContainer {
//      Column(
//        modifier = Modifier
//          .wrapContentHeight()
//          .fillMaxWidth()
//      ) {
//        Spacer(
//          modifier = Modifier
//            .height(60.dp)
//        )
//        Text(
//          text = "COPY FROM HERE",
//          fontSize = 28.sp
//        )
//        Spacer(
//          modifier = Modifier
//            .height(40.dp)
//        )
//        Text(
//          text = """
//        [
//          {"name":"Recipe C1","time":90,"rating":1.5,"steps":"Mix A and B together\nThink about life\n1) Put A in B\n2) Add 200ml of water\n3) Heat up oven to 200°C\nDone now go and eat","link":"https://preppykitchen.com/lemon-merengue-tarts/","ingredients":["carrots","corn","tomatoes"],"tags":["soup","sweet","spicy"]}
//        ]
//        """.trimIndent(),
//          fontSize = 15.sp
//        )
//        Spacer(
//          modifier = Modifier
//            .height(40.dp)
//        )
//        Text(
//          text = """
//        [
//          {"name":"Recipe C1","time":90,"rating":1.5,"steps":"Mix A and B together\nThink about life\n1) Put A in B\n2) Add 200ml of water\n3) Heat up oven to 200°C\nDone now go and eat","link":"https://preppykitchen.com/lemon-merengue-tarts/","ingredients":["carrots","corn","tomatoes"],"tags":["soup","sweet","spicy"]},
//          {"name":"Recipe B2","time":30,"rating":9.5,"link":"https://preppykitchen.com/lemon-merengue-tarts/","ingredients":["carrots","sugar","tomatoes"],"tags":["soup","sweet","sour"]},
//          {"name":"Recipe A3 Very Very Very Very Very Very Very Long","time":120,"rating":6.0,"ingredients":["sugar","eggs","milk"],"tags":["sweet","party","long"]}
//        ]
//        """.trimIndent(),
//          fontSize = 15.sp
//        )
//        Spacer(
//          modifier = Modifier
//            .height(40.dp)
//        )
//      }
//    }
  }
}