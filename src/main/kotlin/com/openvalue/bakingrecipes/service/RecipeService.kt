package com.openvalue.bakingrecipes.service

import com.openvalue.bakingrecipes.api.CreateRecipeRequest
import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.domain.RecipeCategory
import com.openvalue.bakingrecipes.repository.RecipeRepository
import org.springframework.stereotype.Service
import kotlin.time.Duration

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
            // todo validation method in request object
            if (name.isBlank() || description.isBlank() || preparationTime <= Duration.ZERO || cookingTime <= Duration.ZERO || servings <= 0) {
                throw IllegalArgumentException("Invalid recipe data")
            }
            if (ingredients.isNotEmpty()) {
                ingredients.map {
                    Ingredient(null, it.name)
                }
            }
            return Recipe(null, name, description, preparationTime, waitingTime, cookingTime, servings, difficulty, category)
        }
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