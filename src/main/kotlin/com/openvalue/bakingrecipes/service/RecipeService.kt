package com.openvalue.bakingrecipes.service

import com.openvalue.bakingrecipes.api.domain.CreateRecipeRequest
import com.openvalue.bakingrecipes.api.domain.IngredientOfRecipe
import com.openvalue.bakingrecipes.api.domain.StepOfRecipe
import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.domain.QuantifiedIngredient
import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.domain.RecipeCategory
import com.openvalue.bakingrecipes.domain.Step
import com.openvalue.bakingrecipes.domain.StepInRecipe
import com.openvalue.bakingrecipes.repository.RecipeRepository
import org.springframework.stereotype.Service

@Service
class RecipeService(private val recipeRepository: RecipeRepository) {

    fun getAllRecipes(): List<Recipe> = recipeRepository.findAll()

    fun getRecipeById(id: Long): Recipe? = recipeRepository.findById(id).orElse(null)

    fun getRecipesByCategory(category: RecipeCategory): List<Recipe> =
        recipeRepository.findByCategory(category)

    fun searchRecipes(query: String): List<Recipe> =
        recipeRepository.findByNameContainingOrDescriptionContaining(query)

    fun getRecipesByTimeLimit(maxTime: Int): List<Recipe> =
        recipeRepository.findByTotalTimeLimit(maxTime)

    fun getRecipesWithIngredients(ingredients: List<String>, requireAll: Boolean = false): List<Recipe> =
        if (requireAll) recipeRepository.findRecipesWithAllIngredients(ingredients)
        else recipeRepository.findRecipesWithAnyIngredient(ingredients)

    fun createRecipe(request: CreateRecipeRequest): Recipe {
        with(request) {
            if (isInvalid()) {
                throw IllegalArgumentException("Invalid recipe data, one of the following fields is missing: name, description, preparationTime, cookingTime, servings")
            }
            val requiredIngredients = toRequiredIngredients(ingredients)
            val optionalIngredients = toOptionalIngredients(ingredients)
            val recipeSteps = toSteps(steps)
            val recipe = Recipe(name = name, description = description,
                preparationTime = preparationTime, waitingTime = waitingTime, cookingTime = cookingTime,
                servings = servings, difficulty = difficulty, category = category,
                requiredIngredients = requiredIngredients,
                optionalIngredients = optionalIngredients,
                steps = recipeSteps
            )
            return recipeRepository.save(recipe)
        }
    }

    private fun toSteps(steps: List<StepOfRecipe>) = steps.map {
        StepInRecipe(order = it.number, step = Step(instruction = it.instruction, estimatedTime = it.estimatedTime, waitingTime = it.waitingTime))
    }

    private fun toRequiredIngredients(ingredients: List<IngredientOfRecipe>) = if (ingredients.isNotEmpty()) {
        ingredients
            .filterNot { it.isOptional }
            .map { QuantifiedIngredient(null, it.quantity, it.unit, Ingredient(null, it.name)) }
    } else {
        emptyList()
    }

    private fun toOptionalIngredients(ingredients: List<IngredientOfRecipe>) = if (ingredients.isNotEmpty()) {
        ingredients
            .filter { it.isOptional }
            .map { QuantifiedIngredient(null, it.quantity, it.unit, Ingredient(null, it.name)) }
    } else {
        emptyList()
    }

    fun updateRecipe(id: Long, updatedRecipe: Recipe): Recipe? {
        return if (recipeRepository.existsById(id)) {
            recipeRepository.save(updatedRecipe.copy(id = id))
        } else null
    }

    fun deleteRecipe(id: Long): Boolean {
        return if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id)
            true
        } else false
    }
}