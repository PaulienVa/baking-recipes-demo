package com.openvalue.bakingrecipes.repository.projections

import com.openvalue.bakingrecipes.domain.Ingredient

data class MostUsedIngredientRow(
    val ingredient: Ingredient,
    val recipeCount: Long
)