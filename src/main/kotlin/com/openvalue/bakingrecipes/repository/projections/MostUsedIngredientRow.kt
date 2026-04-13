package com.openvalue.bakingrecipes.repository.projections

data class MostUsedIngredientRow(
    val ingredientName: String,
    val recipeCount: Long
)