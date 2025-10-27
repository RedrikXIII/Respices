package com.example.respices

import android.content.ContentValues
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.storage.db.AppDatabase
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag
import com.example.respices.storage.repositories.RecipeRepository
import com.example.respices.support.enums.Screen
import com.example.respices.support.extensions.replaceTyping
import com.example.respices.support.extensions.toString2
import com.example.respices.support.services.GlobalState
import com.example.respices.support.services.LoggerService
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
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    //Toast.makeText(this, "App Start", Toast.LENGTH_SHORT).show()

    //setupCrashLogging()

    //Toast.makeText(this, "Crash Log", Toast.LENGTH_SHORT).show()

    LoggerService.init(this)
    LoggerService.log("MainActivity: Logger started", this)

    //Toast.makeText(this, "Logger Service", Toast.LENGTH_SHORT).show()

    setContent {
      RespicesTheme {

        val context = LocalContext.current
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
            ) {
              TopBar()

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .fillMaxHeight()
              ) {
                val scrollState = rememberScrollState()

                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .verticalScroll(scrollState)
                ) {
                  when (GlobalState.currentScreen.value) {
                    Screen.START_PAGE -> StartPage()
                    Screen.MEAL_LIST -> MealList()
                    Screen.MEAL_VIEW -> MealView()
                    Screen.MEAL_EDIT -> MealEdit()
                    Screen.CSV_VIEW -> CSVView()
                    Screen.MEAL_DELETE -> MealDelete()
                  }
                }

                GlobalSearchBar()
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
          name = "Recipe 1",
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
          name = "Recipe 2",
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
          name = "Recipe 3",
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

  fun TestLoadDatabase(recipeViewModel: RecipeViewModel) {
    LoggerService.log("MainActivity: test loading the database...", this)

    lifecycleScope.launch {
      recipeViewModel.allMeals.collect { meals ->
        var result: String = ""
        meals.forEach { recipe ->
          result += recipe.toString2() + "\n"
        }
        Log.d("db test recipes", result)
        LoggerService.log("MainActivity: loaded all meals size ${meals.size}", applicationContext)
      }
    }

    lifecycleScope.launch {
      recipeViewModel.allIngredients.collect { ings ->
        var result: String = ""
        ings.forEach { ing ->
          result += ing.name + "\n"
        }
        Log.d("db test ingredients", result)
        LoggerService.log("MainActivity: loaded all ingredients size ${ings.size}", applicationContext)
      }
    }

    lifecycleScope.launch {
      recipeViewModel.allTags.collect { tags ->
        var result: String = ""
        tags.forEach { tag ->
          result += tag.name + "\n"
        }
        Log.d("db test tags", result)
        LoggerService.log("MainActivity: loaded all tags size ${tags.size}", applicationContext)
      }
    }
  }

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

