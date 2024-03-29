package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.ui.theme.*

// Check if the input is not just a single "0", and if it has leading zeros
fun removeLeadingZeros(input: String): String {
    return if (input != "0" && input.startsWith("0")) {
        input.trimStart('0').ifEmpty { "0" }
    } else {
        input 
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AddIngredientPopUpWindow(){
    var showDialog by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }
    val nutritionLabels = listOf("Serving Size", "Calories", "Carbs", "Fat", "Protein")
    var showIngredientSearch by remember { mutableStateOf(false) }

    // TODO: Integrate backend data fields here
    val nutritionSize = remember { mutableStateListOf("0", "0", "0", "0", "0") }
    val nutritionUnit = listOf("g", "kcal", "g", "g", "g")
    val ingredientDatabase = listOf("Apple", "Banana", "Carrot", "Date", "Eggplant", "apes")
    // TODO: (Optional) filter ingredients here or at backend
    val filteredIngredients = remember(searchText) {
        ingredientDatabase.filter { it.contains(searchText, ignoreCase = true) }
    }


    Button(onClick = { showDialog = true }) {
        Text("Show Pop-Up")
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        shape = RoundedCornerShape(20.dp)
                        clip = true
                    }
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(PrimaryPurple, PrimaryPink),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
                    .padding(2.5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bar for searching ingredients
                        SearchBar(
                            query = searchText,
                            onQueryChange = {it -> searchText = it},
                            onSearch = {},
                            active = showIngredientSearch,
                            onActiveChange = {showIngredientSearch = true},
                            placeholder = {
                                Row {
                                    Icon(
                                        Icons.Filled.Search,
                                        contentDescription = "search",
                                        tint = SecondaryGrey
                                    )
                                    Text(
                                        text = " Find an Ingredient...",
                                        color = SecondaryGrey,
                                        style = TextStyle(fontSize = 17.sp)
                                    )
                                }
                            }
                        ) {

                            LazyColumn{
                                items(filteredIngredients) { ingredient ->
                                    Text(
                                        text = ingredient,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                searchText = ingredient
                                                showIngredientSearch = false
                                                // TODO: Apply according nutrition facts to text fields below
                                            }
                                            .padding(8.dp)
                                    )
                                    Divider()
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nutrition Facts
                        (0..4).forEach { index ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = nutritionLabels[index],
                                    color = SecondaryGrey,
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.weight(1.5f)
                                )

                                TextField(
                                    value = nutritionSize[index],
                                    onValueChange = { newValue ->
                                        nutritionSize[index] = removeLeadingZeros(newValue)
                                    },
                                    modifier = Modifier
                                        .weight(0.5f),
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = PrimaryPurple,
                                        unfocusedIndicatorColor = SecondaryGrey,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent
                                    )
                                )

                                Text(
                                    text = nutritionUnit[index],
                                    style = TextStyle(fontSize = 18.sp),
                                    color = SecondaryGrey,
                                    modifier = Modifier
                                        .weight(0.4f)
                                        .padding(start = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Add Button at the bottom
                        Button(
                            onClick = {
                                showDialog = false
                                // TODO: Add ingredient to ingredient list
                            },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.5f),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPurple
                            )
                        ) {
                            Text("Add", color = Color.White)
                        }

                    }
                }

                // Close pop up button
                IconButton(
                    onClick = { showDialog = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        "close",
                        tint = PrimaryPink)
                }
            }
        }
    }
}