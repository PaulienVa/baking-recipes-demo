package com.openvalue.bakingrecipes.service

import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.domain.RecipeCategory
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

    fun createRecipe(recipe: Recipe): Recipe = recipeRepository.save(recipe)

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