package com.openvalue.bakingrecipes.controller

import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.domain.IngredientType
import com.openvalue.bakingrecipes.repository.IngredientRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = ["*"])
class IngredientController(private val ingredientRepository: IngredientRepository) {

    @GetMapping
    fun getAllIngredients(): ResponseEntity<List<Ingredient>> {
        val ingredients = ingredientRepository.findAll()
        return ResponseEntity.ok(ingredients)
    }

    @GetMapping("/search")
    fun searchIngredients(@RequestParam name: String): ResponseEntity<List<Ingredient>> {
        val ingredients = ingredientRepository.findByNameContainingIgnoreCase(name)
        return ResponseEntity.ok(ingredients)
    }

    @GetMapping("/type/{type}")
    fun getIngredientsByType(@PathVariable type: IngredientType): ResponseEntity<List<Ingredient>> {
        val ingredients = ingredientRepository.findByType(type)
        return ResponseEntity.ok(ingredients)
    }

    @GetMapping("/popular")
    fun getMostUsedIngredients(): ResponseEntity<List<Map<String, Any>>> {
        val ingredients = ingredientRepository.findMostUsedIngredients()
        return ResponseEntity.ok(ingredients)
    }

    @PostMapping
    fun createIngredient(@RequestBody ingredient: Ingredient): ResponseEntity<Ingredient> {
        val createdIngredient = ingredientRepository.save(ingredient)
        return ResponseEntity.ok(createdIngredient)
    }
}