package com.openvalue.bakingrecipes.service

import com.openvalue.bakingrecipes.domain.*
import com.openvalue.bakingrecipes.repository.RecipeRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class RecipeServiceTest {

    @Mock
    private lateinit var recipeRepository: RecipeRepository

    @InjectMocks
    private lateinit var recipeService: RecipeService

    private lateinit var testRecipe: Recipe

    @BeforeEach
    fun setUp() {
        testRecipe = Recipe(
            id = 1L,
            name = "Chocolate Chip Cookies",
            description = "Delicious homemade cookies",
            preparationTime = 15,
            cookingTime = 12,
            servings = 24,
            difficulty = Difficulty.EASY,
            category = RecipeCategory.COOKIES
        )
    }

    @Test
    fun `should return all recipes when getAllRecipes is called`() {
        val recipes = listOf(testRecipe)
        `when`(recipeRepository.findAll()).thenReturn(recipes)

        val result = recipeService.getAllRecipes()

        assertThat(result).isEqualTo(recipes)
        verify(recipeRepository).findAll()
    }

    @Test
    fun `should return recipe by id when recipe exists`() {
        `when`(recipeRepository.findById(1L)).thenReturn(Optional.of(testRecipe))

        val result = recipeService.getRecipeById(1L)

        assertThat(result).isEqualTo(testRecipe)
        verify(recipeRepository).findById(1L)
    }

    @Test
    fun `should return null when recipe does not exist`() {
        `when`(recipeRepository.findById(999L)).thenReturn(Optional.empty())

        val result = recipeService.getRecipeById(999L)

        assertThat(result).isNull()
        verify(recipeRepository).findById(999L)
    }

    @Test
    fun `should return recipes by category`() {
        val recipes = listOf(testRecipe)
        `when`(recipeRepository.findByCategory(RecipeCategory.COOKIES)).thenReturn(recipes)

        val result = recipeService.getRecipesByCategory(RecipeCategory.COOKIES)

        assertThat(result).isEqualTo(recipes)
        verify(recipeRepository).findByCategory(RecipeCategory.COOKIES)
    }

    @Test
    fun `should search recipes by query`() {
        val recipes = listOf(testRecipe)
        `when`(recipeRepository.findByNameContainingOrDescriptionContaining("chocolate")).thenReturn(recipes)

        val result = recipeService.searchRecipes("chocolate")

        assertThat(result).isEqualTo(recipes)
        verify(recipeRepository).findByNameContainingOrDescriptionContaining("chocolate")
    }

    @Test
    fun `should create recipe successfully`() {
        `when`(recipeRepository.save(testRecipe)).thenReturn(testRecipe)

        val result = recipeService.createRecipe(testRecipe)

        assertThat(result).isEqualTo(testRecipe)
        verify(recipeRepository).save(testRecipe)
    }

    @Test
    fun `should update recipe when recipe exists`() {
        val updatedRecipe = testRecipe.copy(name = "Updated Cookie Recipe")
        `when`(recipeRepository.existsById(1L)).thenReturn(true)
        `when`(recipeRepository.save(updatedRecipe)).thenReturn(updatedRecipe)

        val result = recipeService.updateRecipe(1L, updatedRecipe)

        assertThat(result).isEqualTo(updatedRecipe)
        verify(recipeRepository).existsById(1L)
        verify(recipeRepository).save(updatedRecipe)
    }

    @Test
    fun `should return null when updating non-existent recipe`() {
        val updatedRecipe = testRecipe.copy(name = "Updated Cookie Recipe")
        `when`(recipeRepository.existsById(999L)).thenReturn(false)

        val result = recipeService.updateRecipe(999L, updatedRecipe)

        assertThat(result).isNull()
        verify(recipeRepository).existsById(999L)
        verify(recipeRepository, never()).save(any())
    }

    @Test
    fun `should delete recipe when recipe exists`() {
        `when`(recipeRepository.existsById(1L)).thenReturn(true)

        val result = recipeService.deleteRecipe(1L)

        assertThat(result).isTrue()
        verify(recipeRepository).existsById(1L)
        verify(recipeRepository).deleteById(1L)
    }

    @Test
    fun `should return false when deleting non-existent recipe`() {
        `when`(recipeRepository.existsById(999L)).thenReturn(false)

        val result = recipeService.deleteRecipe(999L)

        assertThat(result).isFalse()
        verify(recipeRepository).existsById(999L)
        verify(recipeRepository, never()).deleteById(any())
    }
}