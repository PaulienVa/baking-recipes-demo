package com.openvalue.bakingrecipes.api

import com.openvalue.bakingrecipes.domain.Difficulty
import com.openvalue.bakingrecipes.domain.RecipeCategory
import kotlin.time.Duration

data class CreateRecipeRequest(
    val name: String,
    val description: String,
    val preparationTime: Duration,
    val waitingTime: Duration,
    val cookingTime: Duration,
    val servings: Int,
    val difficulty: Difficulty,
    val category: RecipeCategory,
    val ingredients: List<IngredientOfRecipe>,
    val steps: List<StepOfRecipe>
)

data class IngredientOfRecipe(
    val name: String,
    val quantity: Double,
    val unit: String
)

data class StepOfRecipe(
    val number: Int,
    val instruction: String,
    val estimatedTime: Int? = null
)