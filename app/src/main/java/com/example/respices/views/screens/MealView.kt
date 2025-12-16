package com.example.respices.views.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.respices.R
import com.example.respices.storage.entities.Meal
import com.example.respices.support.utility.HeightBasedRoundedShape
import com.example.respices.ui.theme.RespicesTheme
import com.example.respices.views.elements.HorizontalLine

@Composable
fun MealView(
  mealI: Meal?
) {
  RespicesTheme {
    if (mealI == null) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 60.dp)
          .wrapContentHeight()
      ) {
        Image(
          painter = painterResource(R.drawable.serving_dish_empty),
          contentDescription = "no meal selected",
          modifier = Modifier
            .height(80.dp)
            .width(80.dp)
        )
        Text(
          text = "Oops, no meal selected!",
          fontSize = 26.sp,
          modifier = Modifier
            .padding(top = 30.dp)
        )
      }
    } else mealI.let { meal ->
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
//          .background(
//            color = Color.Yellow
//          )
          .padding(top = 30.dp)
//          .background(
//            color = Color.LightGray
//          )
          .drawBehind {
            val stroke = 1.dp.toPx()
            val c = Color.Black

            // left
            drawLine(
              color = c,
              start = Offset(0f, size.height / 2),
              end = Offset(0f, size.height),
              strokeWidth = stroke
            )

            // right
            drawLine(
              color = c,
              start = Offset(size.width, size.height / 2),
              end = Offset(size.width, size.height),
              strokeWidth = stroke
            )
          }
      ) {
        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )

        Text(
          text = meal.recipe.name,
          fontSize = 30.sp,
          style = TextStyle(
            lineHeight = 34.sp
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier
            .wrapContentWidth()
            .widthIn(max = 300.dp)
            .padding(horizontal = 10.dp)
        )

        HorizontalLine(
          color = Color.Black,
          lineModifier = Modifier
            .height(1.dp)
            .fillMaxWidth(),
          modifier = Modifier
            .weight(1f, fill = true)
            .padding(vertical = 20.dp)
        )
      }
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .drawBehind {
            val stroke = 1.dp.toPx()
            val c = Color.Black

            // left
            drawLine(
              color = c,
              start = Offset(0f, 0f),
              end = Offset(0f, size.height),
              strokeWidth = stroke
            )

            // bottom
            drawLine(
              color = c,
              start = Offset(0f, size.height),
              end = Offset(size.width, size.height),
              strokeWidth = stroke
            )

            // right
            drawLine(
              color = c,
              start = Offset(size.width, 0f),
              end = Offset(size.width, size.height),
              strokeWidth = stroke
            )
          }
      ) {
        Row(
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .width(250.dp)
            .wrapContentHeight()
            .padding(top = 40.dp, bottom = 10.dp)
        ) {
          HorizontalLine(
            color = Color.Black,
            lineModifier = Modifier
              .height(1.dp)
              .fillMaxWidth(),
            modifier = Modifier
              .weight(1f, fill = true)
              .padding(vertical = 20.dp)
          )

          Text(
            text = "Ingredients",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
              .wrapContentWidth()
              .padding(horizontal = 10.dp)
          )

          HorizontalLine(
            color = Color.Black,
            lineModifier = Modifier
              .height(1.dp)
              .fillMaxWidth(),
            modifier = Modifier
              .weight(1f, fill = true)
              .padding(vertical = 20.dp)
          )
        }

        FlowRow(
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier
            .fillMaxWidth()
        ) {
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
                .padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
              Text(
                text = ing,
                fontSize = 20.sp
              )
            }
          }
        }

        Row(
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .width(250.dp)
            .wrapContentHeight()
            .padding(top = 40.dp, bottom = 10.dp)
        ) {
          HorizontalLine(
            color = Color.Black,
            lineModifier = Modifier
              .height(1.dp)
              .fillMaxWidth(),
            modifier = Modifier
              .weight(1f, fill = true)
              .padding(vertical = 20.dp)
          )

          Text(
            text = "Tags",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
              .wrapContentWidth()
              .padding(horizontal = 10.dp)

          )

          HorizontalLine(
            color = Color.Black,
            lineModifier = Modifier
              .height(1.dp)
              .fillMaxWidth(),
            modifier = Modifier
              .weight(1f, fill = true)
              .padding(vertical = 20.dp)
          )
        }


        FlowRow(
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier
            .fillMaxWidth()
        ) {
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
                .padding(vertical = 10.dp, horizontal = 12.dp)
            ) {
              Text(
                text = tag,
                fontSize = 20.sp
              )
            }
          }
        }

        Row(
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .width(250.dp)
            .wrapContentHeight()
            .padding(top = 40.dp, bottom = 10.dp)
        ) {
          HorizontalLine(
            color = Color.Black,
            lineModifier = Modifier
              .height(1.dp)
              .fillMaxWidth(),
            modifier = Modifier
              .weight(1f, fill = true)
              .padding(vertical = 20.dp)
          )

          Text(
            text = "Time & Rating",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
              .wrapContentWidth()
              .padding(horizontal = 10.dp)

          )

          HorizontalLine(
            color = Color.Black,
            lineModifier = Modifier
              .height(1.dp)
              .fillMaxWidth(),
            modifier = Modifier
              .weight(1f, fill = true)
              .padding(vertical = 20.dp)
          )
        }

        Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .padding(top = 15.dp)
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
                .padding(end = 10.dp, bottom = 4.dp)
                .size(45.dp)
            )

            val fHours: String = "${meal.recipe.time.div(60)}"
            val fMinutes: String =
              "${meal.recipe.time.mod(60).div(10)}${meal.recipe.time.mod(60).mod(10)}"

            Text(
              text = "$fHours:$fMinutes",
              fontSize = 34.sp
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
                .size(45.dp)
            )

            val fRating: Double = (meal.recipe.rating * 10).div(10)

            Text(
              text = "${if ((fRating * 10).mod(10.0) != 0.0) fRating else fRating.toInt()}/10",
              fontSize = 34.sp
            )
          }
        }

        if (meal.recipe.steps != "") {
          Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .width(250.dp)
              .wrapContentHeight()
              .padding(top = 40.dp, bottom = 10.dp)
          ) {
            HorizontalLine(
              color = Color.Black,
              lineModifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
              modifier = Modifier
                .weight(1f, fill = true)
                .padding(vertical = 20.dp)
            )

            Text(
              text = "Recipe",
              fontSize = 24.sp,
              textAlign = TextAlign.Center,
              modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp)
            )

            HorizontalLine(
              color = Color.Black,
              lineModifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
              modifier = Modifier
                .weight(1f, fill = true)
                .padding(vertical = 20.dp)
            )
          }

          Text(
            text = meal.recipe.steps,
            fontSize = 20.sp,
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp)
          )
        }

        if (meal.recipe.link != "") {
          Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .width(250.dp)
              .wrapContentHeight()
              .padding(top = 40.dp, bottom = 10.dp)
          ) {
            HorizontalLine(
              color = Color.Black,
              lineModifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
              modifier = Modifier
                .weight(1f, fill = true)
                .padding(vertical = 20.dp)
            )

            Text(
              text = "Link",
              fontSize = 24.sp,
              textAlign = TextAlign.Center,
              modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 10.dp)
            )

            HorizontalLine(
              color = Color.Black,
              lineModifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
              modifier = Modifier
                .weight(1f, fill = true)
                .padding(vertical = 20.dp)
            )
          }

          Text(
            text = buildAnnotatedString {
              withLink(
                LinkAnnotation.Url(
                  meal.recipe.link,
                  TextLinkStyles(
                    style = SpanStyle(
                      color = Color.Blue,
                      textDecoration = TextDecoration.Underline
                    )
                  )
                )
              ) {
                append(meal.recipe.link)
              }
            },
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 10.dp)
              .padding(top = 10.dp)
          )
        }

        Spacer(
          modifier = Modifier
            .height(50.dp)
        )
      }

      Spacer(
        modifier = Modifier
          .height(70.dp)
      )
    }
  }
}
