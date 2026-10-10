package com.diabetesapp.data

data class Food(
    val id: String,
    val name: String,
    val barcode: String,
    val carbs: Double,
    val protein: Double,
    val fat: Double,
    val calories: Int,
    val servingUnit: String = "100 g",
    val category: String = "Food"
)

data class MealEntry(
    val food: Food,
    val quantity: Double
)
