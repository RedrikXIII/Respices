package com.example.respices

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.respices.storage.db.AppDatabase
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag
import com.example.respices.storage.repositories.RecipeRepository
import com.example.respices.support.enums.Screen
import com.example.respices.support.extensions.toString2
import com.example.respices.support.services.GlobalState
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.viewmodel.LocalRecipeViewModel
import com.example.respices.viewmodel.RecipeViewModel
import com.example.respices.viewmodel.RecipeViewModelFactory
import com.example.respices.views.elements.GlobalSearchBar
import com.example.respices.views.elements.TopBar
import com.example.respices.views.screens.CSVView
import com.example.respices.views.screens.MealDelete
import com.example.respices.views.screens.MealEdit
import com.example.respices.views.screens.MealList
import com.example.respices.views.screens.MealView
import com.example.respices.views.screens.StartPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    PopulateDatabase()
    TestLoadDatabase()



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
            db.crossRefDao()
          )
        }

        val recipeViewModel: RecipeViewModel = viewModel(
          viewModelStoreOwner = activity,
          factory = RecipeViewModelFactory(repository)
        )

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

  fun PopulateDatabase() {
    val db = AppDatabase.getInstance(this)
    val repository = RecipeRepository(
      db.recipeDao(),
      db.ingredientDao(),
      db.tagDao(),
      db.crossRefDao()
    )

    CoroutineScope(Dispatchers.IO).launch {
      db.clearAllTables()

      repository.insertRWIAT(
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
      repository.insertRWIAT(
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
      repository.insertRWIAT(
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
    }
  }

  fun TestLoadDatabase() {
    val db = AppDatabase.getInstance(this)
    val repository = RecipeRepository(
      db.recipeDao(),
      db.ingredientDao(),
      db.tagDao(),
      db.crossRefDao()
    )
    val viewModel = ViewModelProvider(
      this,
      RecipeViewModelFactory(repository)
    )[RecipeViewModel::class.java]

    viewModel.loadAllRecipes()
    viewModel.loadAllIngredients()
    viewModel.loadAllTags()

    lifecycleScope.launch {
      viewModel.allRecipes.collect { recipes ->
        var result: String = ""
        recipes.forEach { recipe ->
          result += recipe.toString2() + "\n"
        }
        Log.d("db test recipes", result)
      }
    }

    lifecycleScope.launch {
      viewModel.allIngredients.collect { ings ->
        var result: String = ""
        ings.forEach { ing ->
          result += ing.name + "\n"
        }
        Log.d("db test ingredients", result)
      }
    }

    lifecycleScope.launch {
      viewModel.allTags.collect { tags ->
        var result: String = ""
        tags.forEach { tag ->
          result += tag.name + "\n"
        }
        Log.d("db test tags", result)
      }
    }
  }
}

