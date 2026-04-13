package com.openvalue.bakingrecipes.service

import com.openvalue.bakingrecipes.api.domain.MostUsedIngredient
import com.openvalue.bakingrecipes.api.domain.SingleIngredient
import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.repository.IngredientRepository
import org.springframework.stereotype.Service

@Service
class IngredientService(private val ingredientRepository: IngredientRepository) {

    fun allIngredients(): List<SingleIngredient> = ingredientRepository.findAll().map { toIngredientOfRecipe(it) }

    fun findIngredientsByName(name: String): List<SingleIngredient> =
        ingredientRepository.findByNameContainingIgnoreCase(name).map { toIngredientOfRecipe(it) }

    fun findTheMostUsedIngredients(): List<MostUsedIngredient> =
        ingredientRepository.findMostUsedIngredients().map { MostUsedIngredient(it.ingredientName, it.recipeCount.toInt()) }

    private fun toIngredientOfRecipe(ingredient: Ingredient) = SingleIngredient(name = ingredient.name)
}
