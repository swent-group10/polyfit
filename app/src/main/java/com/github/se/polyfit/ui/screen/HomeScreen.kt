package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview
fun HomeScreen() {
  Scaffold(
      modifier = Modifier,
      topBar ={
          Box(modifier= Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter){

             Title(modifier = Modifier, 35.sp)
          }
      },
      content = { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxWidth()) {
          LazyColumn(contentPadding = PaddingValues(horizontal = 30.dp, vertical = 20.dp)) {
              item{
                  Text(
                      text = "Welcome Back, User432!",
                      fontSize = 20.sp,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.primary

                  )
              }
            item {
              OutlinedCard(
                  modifier =
                      Modifier.align(Alignment.Center)
                          .size(width = 350.dp, height = 210.dp),
                  border =
                      BorderStroke(
                          2.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.inversePrimary, MaterialTheme.colorScheme.primary))),
                  colors = CardDefaults.cardColors(Color.Transparent)) {
                    calorieCardContent()
                  }
            }
            item {
              OutlinedCard(
                  modifier =
                      Modifier.align(Alignment.Center)
                          .padding(top = 10.dp)
                          .size(width = 350.dp, height = 100.dp),
                  border =
                      BorderStroke(
                          2.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.inversePrimary, MaterialTheme.colorScheme.primary))),
                  colors = CardDefaults.cardColors(Color.Transparent)) {

                    //
                  }
            }
          }
        }
      })
}


@Composable
fun Title(modifier: Modifier, fontSize : TextUnit){
    val shape = RoundedCornerShape(35)

    Box(
        modifier = Modifier.clip(shape)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Text(
            text = "Polyfit",
            fontSize = fontSize,
            modifier = modifier,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun calorieCardContent(){
    Box(modifier = Modifier.fillMaxSize()){
        Text(
            text = "Calories Goal",
            modifier = Modifier.align(Alignment.TopStart).padding(start = 10.dp, top = 10.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Text(
            text = "756/",
            modifier = Modifier.align(Alignment.TopStart).padding(start = 10.dp, top = 50.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "2200",
            modifier = Modifier.align(Alignment.TopStart).padding(start = 90.dp, top = 68.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Breakfast",
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 90.dp, top = 50.dp),
            color = Color.Magenta,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Lunch",
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 112.dp, top = 70.dp),
            color = Color.Magenta,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Dinner",
            modifier = Modifier.align(Alignment.TopEnd).padding(end = 109.dp, top = 90.dp),
            color = Color.Magenta,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Track your meals",
            modifier = Modifier.align(Alignment.CenterStart).padding(top = 30.dp, start = 10.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )

    }
}