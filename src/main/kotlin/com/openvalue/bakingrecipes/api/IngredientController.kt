package com.openvalue.bakingrecipes.api

import com.openvalue.bakingrecipes.api.domain.IngredientOfRecipe
import com.openvalue.bakingrecipes.api.domain.MostUsedIngredient
import com.openvalue.bakingrecipes.api.domain.SingleIngredient
import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.repository.IngredientRepository
import com.openvalue.bakingrecipes.service.IngredientService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ingredients")
class IngredientController(private val ingredientService: IngredientService) {

    @GetMapping
    fun getAllIngredients(): ResponseEntity<List<SingleIngredient>> {
        val ingredients = ingredientService.allIngredients()
        return ResponseEntity.ok(ingredients)
    }


    @GetMapping("/search")
    fun searchIngredients(@RequestParam name: String): ResponseEntity<List<SingleIngredient>> {
        val ingredients = ingredientService.findIngredientsByName(name)
        return ResponseEntity.ok(ingredients)
    }

    @GetMapping("/most-used")
    fun searchIngredients(): ResponseEntity<List<MostUsedIngredient>> {
        val ingredients = ingredientService.findTheMostUsedIngredients()
        return ResponseEntity.ok(ingredients)
    }
}