package com.github.se.polyfit.ui.components.selector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PurpleGrey40
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    inputDate: LocalDate? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
  var showDatePicker by remember { mutableStateOf(false) }

  val startDate = inputDate ?: LocalDate.now(ZoneId.systemDefault())
  val initialMillis = startDate.toEpochDay() * 1000 * 60 * 60 * 24
  val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

  val selectedDate =
      LocalDate.ofEpochDay(datePickerState.selectedDateMillis!! / (1000 * 60 * 60 * 24))

  fun onConfirm() {
    onConfirm(selectedDate)
    showDatePicker = false
  }

  Column(modifier = modifier.testTag("DateSelector")) {
    if (title.isNotEmpty()) {
      Text(
          text = title,
          style = titleStyle,
          color = PurpleGrey40,
          modifier = Modifier.padding(16.dp, 0.dp).testTag("Title"))
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth().testTag("DateBox")) {
          TextButton(
              onClick = { showDatePicker = true },
              modifier = Modifier.testTag("ChangeDateButton")) {
                FormatDate(selectedDate)
              }

          if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = { TextButton(onClick = { onConfirm() }) { Text(text = "Done") } },
                dismissButton = {
                  TextButton(onClick = { showDatePicker = false }) { Text(text = "Cancel") }
                },
                modifier = Modifier.testTag("DatePickerDialog")) {
                  DatePicker(
                      state = datePickerState,
                      showModeToggle = false,
                      modifier = Modifier.testTag("DatePicker"))
                }
          }
        }
  }
}

@Composable
private fun FormatDate(date: LocalDate) {
  val day = date.format(DateTimeFormatter.ofPattern("dd"))
  val month = date.format(DateTimeFormatter.ofPattern("MM"))
  val year = date.format(DateTimeFormatter.ofPattern("yyyy"))

  val fontSize = MaterialTheme.typography.titleMedium.fontSize
  val slashColor = PrimaryPink
  val textColor = MaterialTheme.colorScheme.secondary

  Text(
      text = day, textDecoration = TextDecoration.Underline, fontSize = fontSize, color = textColor)
  Text(text = "/", color = slashColor)
  Text(
      text = month,
      textDecoration = TextDecoration.Underline,
      fontSize = fontSize,
      color = textColor)
  Text(text = "/", color = slashColor)
  Text(
      text = year,
      textDecoration = TextDecoration.Underline,
      fontSize = fontSize,
      color = textColor)
}

@Preview
@Composable
fun DateSelectorPreview() {
  DateSelector(onConfirm = {})
}
