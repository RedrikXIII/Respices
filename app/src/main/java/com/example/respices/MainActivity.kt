package com.example.respices

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.storage.db.AppDatabase
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag
import com.example.respices.storage.repositories.RecipeRepository
import com.example.respices.support.enums.Screen
import com.example.respices.support.services.GlobalState
import com.example.respices.support.services.LoggerService
import com.example.respices.support.utility.HeightBasedRoundedShape
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.LocalRecipeViewModel
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.viewmodel.RecipeViewModelFactory
import com.example.respices.views.elements.TopBar
import com.example.respices.views.elements.input.GlobalSearchBar
import com.example.respices.views.screens.CSVView
import com.example.respices.views.screens.MealDelete
import com.example.respices.views.screens.MealEdit
import com.example.respices.views.screens.MealList
import com.example.respices.views.screens.MealView
import com.example.respices.views.screens.StartPage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    WindowCompat.setDecorFitsSystemWindows(window, false)

    //Toast.makeText(this, "App Start", Toast.LENGTH_SHORT).show()

    //setupCrashLogging()

    //Toast.makeText(this, "Crash Log", Toast.LENGTH_SHORT).show()

    LoggerService.init(this)
    LoggerService.log("MainActivity: Logger started", this)

    //Toast.makeText(this, "Logger Service", Toast.LENGTH_SHORT).show()

    setContent {
      RespicesTheme {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val activity = context as ComponentActivity

        val repository = remember {
          val db = AppDatabase.getInstance(context)
          RecipeRepository(
            db.recipeDao(),
            db.ingredientDao(),
            db.tagDao(),
            db.crossRefDao(),
            this
          )
        }

        val recipeViewModel: RecipeViewModel = viewModel(
          viewModelStoreOwner = activity,
          factory = RecipeViewModelFactory(repository, application)
        )

        LaunchedEffect(Unit) {
          PopulateDatabase(recipeViewModel)
        }

        CompositionLocalProvider(LocalRecipeViewModel provides recipeViewModel) {
          Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
          ) {
            Column(
              modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(top = 40.dp)
                .focusable()
            ) {
              TopBar()

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .fillMaxHeight()
              ) {
                val scrollState = rememberScrollState()
                var bottomReached by remember { mutableStateOf(false) }
                val bottomReachedDelay = 250L

                LaunchedEffect(GlobalState.currentScreen.value) {
                  scrollState.scrollTo(0)
                }

                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .verticalScroll(scrollState)
                    .imePadding()
                ) {
                  when (GlobalState.currentScreen.value) {
                    Screen.START_PAGE -> StartPage(bottomReached)
                    Screen.MEAL_LIST -> MealList(bottomReached)
                    Screen.MEAL_VIEW -> MealView()
                    Screen.MEAL_EDIT -> MealEdit()
                    Screen.CSV_VIEW -> CSVView()
                    Screen.MEAL_DELETE -> MealDelete()
                  }
                }

                if (scrollState.value > 50) {
                  Box(
                    modifier = Modifier
                      .height(70.dp)
                      .width(70.dp)
                      .padding(5.dp)
                      .background(
                        color = Color.White,
                        shape = HeightBasedRoundedShape()
                      )
                      .align(Alignment.TopEnd)
                      .clickable {
                        scope.launch {
                          scrollState.animateScrollTo(0)
                        }
                      }
                  ) {
                    Image(
                      painter = painterResource(R.drawable.outline_arrow_circle_up_24),
                      contentDescription = "scroll up",
                      modifier = Modifier
                        .fillMaxSize()
                    )
                  }
                }

                GlobalSearchBar()

                LaunchedEffect(scrollState.value, scrollState.maxValue) {
                  if (scrollState.maxValue == 0) {
                    while (scrollState.maxValue == 0) {
                      bottomReached = true
                      delay(bottomReachedDelay*1/10)
                      bottomReached = false
                      delay(bottomReachedDelay*9/10)
                    }
                  }
                  if (scrollState.value >= scrollState.maxValue && !bottomReached) {
                    while (scrollState.value >= scrollState.maxValue) {
                      bottomReached = true
                      delay(bottomReachedDelay*1/10)
                      bottomReached = false
                      delay(bottomReachedDelay*9/10)
                    }
                  } else if (scrollState.value < scrollState.maxValue) {
                    bottomReached = false
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  fun PopulateDatabase(recipeViewModel: RecipeViewModel) {
    LoggerService.log("MainActivity: populating the database...", this)

    recipeViewModel.clearAll {
      LoggerService.log("MainActivity: deleted all left-over meals", this)

      recipeViewModel.insertMeal(
        Recipe(
          name = "Recipe C1",
          time = 90,
          rating = 1.5,
          link = "https smth smth",
          steps = "step 1 \n step 2"
        ),
        listOf(
          Ingredient(name = "carrots"),
          Ingredient(name = "corn"),
          Ingredient(name = "tomatoes")
        ),
        listOf(
          Tag(name = "soup"),
          Tag(name = "sweet"),
          Tag(name = "spicy")
        )
      )
      recipeViewModel.insertMeal(
        Recipe(
          name = "Recipe B2",
          time = 30,
          rating = 9.5,
          link = "",
          steps = ""
        ),
        listOf(
          Ingredient(name = "carrots"),
          Ingredient(name = "tomatoes"),
          Ingredient(name = "sugar")
        ),
        listOf(
          Tag(name = "sour"),
          Tag(name = "sweet"),
          Tag(name = "soup")
        )
      )
      recipeViewModel.insertMeal(
        Recipe(
          name = "Recipe A3",
          time = 120,
          rating = 6.0,
          link = "https smth smth",
          steps = "step 1 \n step 2"
        ),
        listOf(
          Ingredient(name = "sugar"),
          Ingredient(name = "eggs"),
          Ingredient(name = "milk")
        ),
        listOf(
          Tag(name = "sweet"),
          Tag(name = "party"),
          Tag(name = "long")
        )
      )

      LoggerService.log("MainActivity: populated the database", this)
    }
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  private fun setupCrashLogging() {
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
      // Prepare content values for MediaStore
      val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "respices_crash_log.txt")
        put(MediaStore.Downloads.MIME_TYPE, "text/plain")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
      }

      val resolver = this.contentResolver
      val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

      if (uri != null) {
        resolver.openOutputStream(uri, "wa")?.use { outputStream ->
          val formatter = SimpleDateFormat.getTimeInstance()
          val date = Date()
          val current = formatter.format(date)

          outputStream.write("\n--- Respices Crash at $current ---\n".toByteArray())
          outputStream.write(throwable.stackTraceToString().toByteArray())
        }
      }
    }
  }
}

