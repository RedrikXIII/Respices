package com.example.respices.views.elements.output

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.respices.R
import com.example.respices.storage.entities.Meal
import com.example.respices.support.utility.HeightBasedRoundedShape
import com.example.respices.views.elements.HorizontalLine

@Composable
fun MealDisplay(
  meal: Meal
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(10.dp)
      .border(
        color = Color.Black,
        width = 1.dp
      )
      .padding(vertical = 10.dp, horizontal = 20.dp)
  ) {
    Text(
      text = meal.recipe.name,
      fontSize = 28.sp,
      modifier = Modifier
        .padding(vertical = 10.dp)
    )

    HorizontalLine(
      color = Color.Black,
      lineModifier = Modifier
        .height(1.dp)
        .width(200.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp)
    )

    FlowRow(
      modifier = Modifier
        .fillMaxWidth()
    ) {
      Text(
        text = "Ingredients: ",
        fontSize = 20.sp,
        modifier = Modifier
          .padding(vertical = 12.dp)
          .padding(end = 5.dp)
      )

      meal.ingredients.map { ing -> ing.name }.forEach { ing ->
        Box(
          modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .border(
              width = 1.dp,
              color = Color(0xFF000000),
              shape = HeightBasedRoundedShape()
            )
            .padding(vertical = 7.dp, horizontal = 10.dp)
        ) {
          Text(
            text = ing,
            fontSize = 16.sp
          )
        }
      }
    }

    HorizontalLine(
      color = Color.Black,
      lineModifier = Modifier
        .height(1.dp)
        .width(200.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp)
    )

    FlowRow(
      modifier = Modifier
        .fillMaxWidth()
    ) {
      Text(
        text = "Tags: ",
        fontSize = 20.sp,
        modifier = Modifier
          .padding(vertical = 12.dp)
          .padding(end = 5.dp)
      )

      meal.tags.map { tag -> tag.name }.forEach { tag ->
        Box(
          modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .border(
              width = 1.dp,
              color = Color(0xFF000000),
              shape = HeightBasedRoundedShape()
            )
            .padding(vertical = 7.dp, horizontal = 10.dp)
        ) {
          Text(
            text = tag,
            fontSize = 16.sp
          )
        }
      }
    }

    HorizontalLine(
      color = Color.Black,
      lineModifier = Modifier
        .height(1.dp)
        .width(200.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp)
    )

    Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp, horizontal = 10.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .wrapContentSize()
      ) {
        Image(
          painter = painterResource(R.drawable.outline_alarm_24),
          contentDescription = "clock icon",
          modifier = Modifier
            .padding(end = 10.dp)
            .size(35.dp)
        )

        val fHours: String = "${meal.recipe.time.div(60)}"
        val fMinutes: String = "${meal.recipe.time.mod(60).div(10)}${meal.recipe.time.mod(60).mod(10)}"

        Text(
          text = "$fHours:$fMinutes",
          fontSize = 26.sp
        )
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .wrapContentSize()
          .padding(end = 10.dp)
      ) {
        Image(
          painter = painterResource(R.drawable.outline_kid_star_24),
          contentDescription = "star icon",
          modifier = Modifier
            .padding(end = 10.dp)
            .size(35.dp)
        )

        val fRating: Double = (meal.recipe.rating * 10).div(10)

        Text(
          text = "${if ((fRating * 10).mod(10.0) != 0.0) fRating else fRating.toInt() }/10",
          fontSize = 26.sp
        )
      }
    }
  }
}