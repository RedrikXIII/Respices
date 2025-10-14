package com.example.respices.views.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.respices.ui.theme.RespicesTheme

@Composable
fun MealView() {
  RespicesTheme {
    Text(
      text = "Meal View",
      style = MaterialTheme.typography.titleLarge
    )
  }
}
