package com.example.respices.views.elements.output

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.respices.R
import com.example.respices.support.utility.HeightBasedRoundedShape

@Composable
fun EditableList(
  list: SnapshotStateList<String>,
  arrangement: Arrangement.Horizontal = Arrangement.Start,
  onRemove: (String) -> Unit
) {
  FlowRow(
    horizontalArrangement = arrangement,
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(min = 40.dp)
  ) {
    list.forEachIndexed { index, str ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .wrapContentWidth()
          .padding(vertical = 10.dp)
          .padding(end = 10.dp)
          .border(
            width = 2.dp,
            color = Color(0xFF000000),
            shape = HeightBasedRoundedShape()
          )
          .padding(horizontal = 10.dp)
          .height(40.dp)
      ) {
        Text(
          text = str,
          fontSize = 16.sp,
          modifier = Modifier
            .padding(start = 5.dp)
        )

        Image(
          painter = painterResource(R.drawable.outline_cancel_24),
          contentDescription = "cancel",
          modifier = Modifier
            .padding(start = 10.dp)
            .height(20.dp)
            .aspectRatio(1.0f)
            .clickable {
              list.removeAt(index)
              onRemove.invoke(str)
            }
        )
      }
    }
  }
}