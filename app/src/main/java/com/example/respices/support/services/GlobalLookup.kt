package com.example.respices.support.services

import com.example.respices.R
import com.example.respices.support.enums.Screen

object GlobalLookup {

  // each Triple<> stores data for 3 top bar buttons
  // each Pair<> stores data for a single button
  // first value stores the screen user will be directed to
  // second value stores index of the icon that will be used
  val screenToTopBarScreens: Map<Screen, Triple<Pair<Screen, Int>, Pair<Screen, Int>, Pair<Screen, Int>>> = mapOf(
    Screen.START_PAGE to Triple(
      Pair(Screen.START_PAGE, R.drawable.baseline_home_24),
      Pair(Screen.MEAL_LIST, R.drawable.baseline_format_list_bulleted_24),
      Pair(Screen.MEAL_EDIT, R.drawable.outline_add_24)
    ),
    Screen.MEAL_LIST to Triple(
      Pair(Screen.START_PAGE, R.drawable.baseline_home_24),
      Pair(Screen.FILE_VIEW, R.drawable.outline_file_save_24),
      Pair(Screen.MEAL_EDIT, R.drawable.outline_add_24)
    ),
    Screen.MEAL_VIEW to Triple(
      Pair(Screen.START_PAGE, R.drawable.baseline_home_24),
      Pair(Screen.MEAL_LIST, R.drawable.baseline_format_list_bulleted_24),
      Pair(Screen.MEAL_EDIT, R.drawable.outline_ink_pen_24)
    ),
    Screen.MEAL_EDIT to Triple(
      Pair(Screen.START_PAGE, R.drawable.baseline_home_24),
      Pair(Screen.MEAL_VIEW, R.drawable.outline_assignment_24),
      Pair(Screen.MEAL_DELETE, R.drawable.outline_delete_24)
    ),
    Screen.FILE_VIEW to Triple(
      Pair(Screen.START_PAGE, R.drawable.baseline_home_24),
      Pair(Screen.MEAL_LIST, R.drawable.baseline_format_list_bulleted_24),
      Pair(Screen.MEAL_EDIT, R.drawable.outline_add_24)
    ),
    Screen.MEAL_DELETE to Triple(
      Pair(Screen.START_PAGE, R.drawable.baseline_home_24),
      Pair(Screen.MEAL_LIST, R.drawable.baseline_format_list_bulleted_24),
      Pair(Screen.MEAL_EDIT, R.drawable.outline_ink_pen_24)
    )
  )
}