package com.openvalue.bakingrecipes.service

import com.openvalue.bakingrecipes.api.domain.CreateRecipeRequest
import com.openvalue.bakingrecipes.api.domain.IngredientOfRecipe
import com.openvalue.bakingrecipes.api.domain.SingleRecipe
import com.openvalue.bakingrecipes.api.domain.StepOfRecipe
import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.domain.QuantifiedIngredient
import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.domain.RecipeCategory
import com.openvalue.bakingrecipes.domain.Step
import com.openvalue.bakingrecipes.domain.StepInRecipe
import com.openvalue.bakingrecipes.repository.AuthorRepository
import com.openvalue.bakingrecipes.repository.RecipeRepository
import org.springframework.stereotype.Service

@Service
class RecipeService(private val recipeRepository: RecipeRepository, private val authorRepository: AuthorRepository) {

    fun getAllRecipes(): List<SingleRecipe> = recipeRepository.findAll().map { toSingleRecipe(it) }

    fun searchRecipes(query: String): List<SingleRecipe> =
        recipeRepository.findByNameContainingOrDescriptionContaining(query).map { toSingleRecipe(it) }

    fun getRecipesByTimeLimit(maxTime: Int): List<SingleRecipe> =
        recipeRepository.findByTotalTimeLimit(maxTime).map { toSingleRecipe(it) }


    fun createRecipe(request: CreateRecipeRequest): SingleRecipe {
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
            val save = recipeRepository.save(recipe)
            return SingleRecipe(name = save.name, description = save.description, author = author.name)
        }
    }

    private fun toSingleRecipe(recipe: Recipe): SingleRecipe {
        val nameOfAuthor = authorRepository.findAuthorOfRecipe(recipe.name)
        return SingleRecipe(name = recipe.name, description = recipe.description, author = nameOfAuthor)
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
}