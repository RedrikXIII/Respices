package com.example.respices.views.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.respices.ui.theme.RespicesTheme

@Composable
fun CSVView() {
  RespicesTheme {
    Text(
      text = "CSV View",
      style = MaterialTheme.typography.titleLarge
    )
  }
}