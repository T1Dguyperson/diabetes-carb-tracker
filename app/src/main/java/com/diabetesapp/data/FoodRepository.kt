package com.diabetesapp.data

object FoodRepository {
    val allFoods = listOf(
        Food("pizza", "1234567890123", "Pizza Margherita", 26.0, 12.0, 10.0, 280, "100 g", "Fast Food"),
        Food("banana", "2000000000001", "Banane", 23.0, 1.0, 0.3, 89, "100 g", "Fruit"),
        Food("bread", "2000000000002", "Brot", 48.0, 8.0, 2.0, 240, "100 g", "Bread"),
        Food("rice", "2000000000003", "Reis", 28.0, 2.5, 0.4, 130, "100 g", "Grains"),
        Food("pasta", "2000000000004", "Pasta", 31.0, 7.0, 1.0, 158, "100 g", "Grains"),
        Food("yogurt", "2000000000005", "Joghurt natur", 4.0, 10.0, 3.5, 86, "100 g", "Dairy"),
        Food("apple", "2000000000006", "Apfel", 14.0, 0.3, 0.2, 52, "100 g", "Fruit"),
        Food("milk", "2000000000007", "Milch", 5.0, 3.4, 3.6, 61, "100 ml", "Dairy"),
        Food("chicken", "2000000000008", "Hähnchenbrust", 0.0, 31.0, 3.6, 165, "100 g", "Protein"),
        Food("nuts", "2000000000009", "Mandeln", 10.0, 21.0, 50.0, 579, "100 g", "Nuts")
    )

    fun search(query: String): List<Food> {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) return allFoods

        return allFoods.filter { food ->
            food.name.contains(cleanQuery, ignoreCase = true) ||
                food.barcode.contains(cleanQuery, ignoreCase = true) ||
                food.category.contains(cleanQuery, ignoreCase = true)
        }
    }

    fun findByBarcode(code: String): Food? {
        val clean = code.trim()
        return allFoods.firstOrNull { it.barcode == clean }
    }

    fun getById(id: String): Food? = allFoods.firstOrNull { it.id == id }
}
