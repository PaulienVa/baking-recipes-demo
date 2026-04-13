package com.openvalue.bakingrecipes.api

import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.repository.IngredientRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ingredients")
class IngredientController(private val ingredientRepository: IngredientRepository) {

    @GetMapping
    fun getAllIngredients(): ResponseEntity<List<Ingredient>> {
        val ingredients = ingredientRepository.findAll()
        return ResponseEntity.ok(ingredients)
    }

    @GetMapping("/{name}")
    fun findIngredientsByName(@PathVariable("name") name: String): ResponseEntity<List<Ingredient>> {
        val ingredients = ingredientRepository.findByNameContainingIgnoreCase(name)
        return ResponseEntity.ok(ingredients)
    }

    @GetMapping("/search")
    fun searchIngredients(@RequestParam name: String): ResponseEntity<List<Ingredient>> {
        val ingredients = ingredientRepository.findByNameContainingIgnoreCase(name)
        return ResponseEntity.ok(ingredients)
    }

    @PostMapping
    fun createIngredient(@RequestBody ingredient: Ingredient): ResponseEntity<Ingredient> {
        val createdIngredient = ingredientRepository.save(ingredient)
        return ResponseEntity.ok(createdIngredient)
    }
}