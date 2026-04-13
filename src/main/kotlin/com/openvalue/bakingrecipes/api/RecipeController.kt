package com.openvalue.bakingrecipes.api

import com.openvalue.bakingrecipes.api.domain.CreateRecipeRequest
import com.openvalue.bakingrecipes.api.domain.SingleRecipe
import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.service.AuthorService
import com.openvalue.bakingrecipes.service.RecipeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
class RecipeController(private val recipeService: RecipeService, private val authorService: AuthorService) {

    @GetMapping
    fun getAllRecipes(): ResponseEntity<List<SingleRecipe>> {
        val recipes = recipeService.getAllRecipes()
        return ok(recipes)
    }

    @GetMapping("/search")
    fun searchRecipes(@RequestParam query: String): ResponseEntity<List<SingleRecipe>> {
        val recipes = recipeService.searchRecipes(query)
        return ok(recipes)
    }

    @PostMapping
    fun createRecipe(@Valid @RequestBody recipe: CreateRecipeRequest): ResponseEntity<SingleRecipe> {
        val createdRecipe = recipeService.createRecipe(recipe)
        val author = authorService.saveAuthor(recipe.author)
        return status(HttpStatus.CREATED).body(createdRecipe)
    }
}