package com.diabetesapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diabetesapp.data.Food
import com.diabetesapp.data.FoodRepository
import com.diabetesapp.data.MealEntry
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodTrackerApp() {
    var searchText by rememberSaveable { mutableStateOf("") }
    var barcodeText by rememberSaveable { mutableStateOf("") }
    var quantityText by rememberSaveable { mutableStateOf("100") }
    var selectedFoodId by rememberSaveable { mutableStateOf(FoodRepository.allFoods.first().id) }
    val mealHistory = remember { mutableStateListOf<MealEntry>() }

    val filteredFoods = remember(searchText) {
        FoodRepository.search(searchText)
    }

    val selectedFood = FoodRepository.getById(selectedFoodId) ?: FoodRepository.allFoods.first()
    val quantity = quantityText.toDoubleOrNull() ?: 0.0

    val totalCarbs = mealHistory.sumOf { it.food.carbs * (it.quantity / 100.0) }
    val totalProtein = mealHistory.sumOf { it.food.protein * (it.quantity / 100.0) }
    val totalFat = mealHistory.sumOf { it.food.fat * (it.quantity / 100.0) }
    val totalCalories = mealHistory.sumOf { it.food.calories.toDouble() * (it.quantity / 100.0) }

    val selectedMealCarbs = selectedFood.carbs * (quantity / 100.0)
    val selectedMealProtein = selectedFood.protein * (quantity / 100.0)
    val selectedMealFat = selectedFood.fat * (quantity / 100.0)
    val selectedCalories = selectedFood.calories * (quantity / 100.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Typ 1 Ernährung") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Kohlenhydrate & Nährstoffe",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Warnung: Diese App hilft bei Lebensmitteln, Kohlenhydraten und Nährstoffen. Sie ersetzt keine ärztliche Beratung.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Produkt suchen") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = barcodeText,
                    onValueChange = { barcodeText = it },
                    label = { Text("Barcode") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = {
                        val found = FoodRepository.findByBarcode(barcodeText)
                        if (found != null) {
                            selectedFoodId = found.id
                            searchText = found.name
                        }
                    }
                ) {
                    Text("Check")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        barcodeText = "1234567890123"
                        val found = FoodRepository.findByBarcode(barcodeText)
                        if (found != null) {
                            selectedFoodId = found.id
                            searchText = found.name
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Demo Barcode")
                }
                Button(
                    onClick = {
                        val product = FoodRepository.search(searchText).firstOrNull()
                        if (product != null) {
                            selectedFoodId = product.id
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Use food")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = selectedFood.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Kategorie: ${selectedFood.category}")
                    Text("Barcode: ${selectedFood.barcode}")

                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        label = { Text("Menge in g") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Kohlenhydrate: ${selectedMealCarbs.roundToInt()} g")
                        Text("Protein: ${selectedMealProtein.roundToInt()} g")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Fett: ${selectedMealFat.roundToInt()} g")
                        Text("Kalorien: ${selectedCalories.roundToInt()} kcal")
                    }

                    Button(
                        onClick = {
                            val validQuantity = quantityText.toDoubleOrNull() ?: return@Button
                            if (validQuantity > 0) {
                                mealHistory.add(MealEntry(selectedFood, validQuantity))
                                quantityText = "100"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text("Mahlzeit hinzufügen")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Heute total",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Kohlenhydrate: ${totalCarbs.roundToInt()} g")
                    Text("Protein: ${totalProtein.roundToInt()} g")
                    Text("Fett: ${totalFat.roundToInt()} g")
                    Text("Kalorien: ${totalCalories.roundToInt()} kcal")
                }
            }

            Text(
                text = "Letzte Mahlzeiten",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(mealHistory) { meal ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = meal.food.name,
                                fontWeight = FontWeight.Bold
                            )
                            Text("${meal.quantity.roundToInt()} g • KH ${((meal.food.carbs * (meal.quantity / 100.0))).roundToInt()} g")
                            Text("Protein ${(meal.food.protein * (meal.quantity / 100.0)).roundToInt()} g • Fett ${(meal.food.fat * (meal.quantity / 100.0)).roundToInt()} g")
                        }
                    }
                }
            }

            if (filteredFoods.isNotEmpty()) {
                Text("Vorschläge", style = MaterialTheme.typography.titleSmall)
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredFoods.take(5)) { food ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (food.id == selectedFoodId) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedFoodId = food.id
                                    searchText = food.name
                                }
                                .padding(12.dp)
                        ) {
                            Text(food.name)
                        }
                    }
                }
            }
        }
    }
}
