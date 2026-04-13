package com.openvalue.bakingrecipes.api

import com.openvalue.bakingrecipes.api.domain.CreateRecipeRequest
import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.service.AuthorService
import com.openvalue.bakingrecipes.service.RecipeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
class RecipeController(private val recipeService: RecipeService, private val authorService: AuthorService) {

    @GetMapping
    fun getAllRecipes(): ResponseEntity<List<Recipe>> {
        val recipes = recipeService.getAllRecipes()
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

    @PostMapping
    fun createRecipe(@Valid @RequestBody recipe: CreateRecipeRequest): ResponseEntity<Recipe> {
        val createdRecipe = recipeService.createRecipe(recipe)
        val author = authorService.saveAuthor(recipe.author)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe)
    }
}