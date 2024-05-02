package com.github.se.polyfit.ui.components.postinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.github.se.polyfit.R
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.post.Post

@Composable
fun TextPostInfo(modifier: Modifier = Modifier, post: Post) {

  Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
    TextPost(
        text = ContextCompat.getString(LocalContext.current, R.string.description),
        modifier = modifier.testTag("DescriptionTitle"),
        bold = true)

    TextPost(
        text = post.createdAt.dayOfMonth.toString() + " " + post.createdAt.month.toString(),
        modifier = modifier.testTag("Date"),
        bold = false)
  }

  TextPost(text = post.description, modifier = modifier.testTag("Description"), bold = false)

  TextPost(
      text = ContextCompat.getString(LocalContext.current, R.string.nutrient),
      modifier = modifier.testTag("NutrientTitle"),
      bold = true)

  post.meal.ingredients.forEach { it.nutritionalInformation.nutrients.forEach { NutrientInfo(it) } }
}

@Composable
private fun TextPost(text: String, modifier: Modifier = Modifier, bold: Boolean = false) {
  val style = MaterialTheme.typography.bodyMedium
  val fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal

  Text(
      text,
      modifier = modifier,
      color = MaterialTheme.colorScheme.onSurface,
      style = style,
      fontWeight = fontWeight)
}

@Composable
private fun NutrientInfo(
    nutrient: Nutrient,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
  Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.fillMaxWidth().padding(8.dp, 2.dp)) {
        Text(nutrient.getFormattedName(), style = style)
        Text(nutrient.getFormattedAmount(), style = style)
      }
}
