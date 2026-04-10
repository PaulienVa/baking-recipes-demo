package com.openvalue.bakingrecipes.api

import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.domain.RecipeCategory
import com.openvalue.bakingrecipes.service.RecipeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = ["*"])
class RecipeController(private val recipeService: RecipeService) {

    @GetMapping
    fun getAllRecipes(): ResponseEntity<List<Recipe>> {
        val recipes = recipeService.getAllRecipes()
        return ResponseEntity.ok(recipes)
    }

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable id: Long): ResponseEntity<Recipe> {
        val recipe = recipeService.getRecipeById(id)
        return if (recipe != null) {
            ResponseEntity.ok(recipe)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/category/{category}")
    fun getRecipesByCategory(@PathVariable category: RecipeCategory): ResponseEntity<List<Recipe>> {
        val recipes = recipeService.getRecipesByCategory(category)
        return ResponseEntity.ok(recipes)
    }

    @GetMapping("/search")
    fun searchRecipes(@RequestParam query: String): ResponseEntity<List<Recipe>> {
        val recipes = recipeService.searchRecipes(query)
        return ResponseEntity.ok(recipes)
    }

    @GetMapping("/time-limit")
    fun getRecipesByTimeLimit(@RequestParam maxTime: Int): ResponseEntity<List<Recipe>> {
        val recipes = recipeService.getRecipesByTimeLimit(maxTime)
        return ResponseEntity.ok(recipes)
    }

    @GetMapping("/ingredients")
    fun getRecipesWithIngredients(
        @RequestParam ingredients: List<String>,
        @RequestParam(defaultValue = "false") requireAll: Boolean
    ): ResponseEntity<List<Recipe>> {
        val recipes = recipeService.getRecipesWithIngredients(ingredients, requireAll)
        return ResponseEntity.ok(recipes)
    }

    @PostMapping
    fun createRecipe(@Valid @RequestBody recipe: CreateRecipeRequest): ResponseEntity<Recipe> {
        val createdRecipe = recipeService.createRecipe(recipe)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe)
    }

    @PutMapping("/{id}")
    fun updateRecipe(
        @PathVariable id: Long,
        @Valid @RequestBody recipe: Recipe
    ): ResponseEntity<Recipe> {
        val updatedRecipe = recipeService.updateRecipe(id, recipe)
        return if (updatedRecipe != null) {
            ResponseEntity.ok(updatedRecipe)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteRecipe(@PathVariable id: Long): ResponseEntity<Void> {
        val deleted = recipeService.deleteRecipe(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}