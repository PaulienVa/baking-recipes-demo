package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.Neo4jContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@Transactional
class RecipeRepositoryTest {

    companion object {
        @Container
        @JvmStatic
        private val neo4jContainer = Neo4jContainer<Nothing>("neo4j:5.15")
            .withAdminPassword("test-password")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.neo4j.uri") { neo4jContainer.boltUrl }
            registry.add("spring.neo4j.authentication.username") { "neo4j" }
            registry.add("spring.neo4j.authentication.password") { neo4jContainer.adminPassword }
        }
    }

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Autowired
    private lateinit var ingredientRepository: IngredientRepository

    private lateinit var cookieRecipe: Recipe
    private lateinit var cakeRecipe: Recipe
    private lateinit var flour: Ingredient
    private lateinit var sugar: Ingredient

    @BeforeEach
    fun setUp() {
        recipeRepository.deleteAll()
        ingredientRepository.deleteAll()

        flour = ingredientRepository.save(Ingredient(name = "Flour", type = IngredientType.FLOUR))
        sugar = ingredientRepository.save(Ingredient(name = "Sugar", type = IngredientType.SUGAR))

        cookieRecipe = Recipe(
            name = "Chocolate Chip Cookies",
            description = "Classic cookies with chocolate chips",
            preparationTime = 15,
            cookingTime = 12,
            servings = 24,
            difficulty = Difficulty.EASY,
            category = RecipeCategory.COOKIES,
            ingredients = setOf(flour, sugar)
        )

        cakeRecipe = Recipe(
            name = "Vanilla Cake",
            description = "Simple vanilla flavored cake",
            preparationTime = 20,
            cookingTime = 30,
            servings = 8,
            difficulty = Difficulty.MEDIUM,
            category = RecipeCategory.CAKE,
            ingredients = setOf(flour, sugar)
        )

        recipeRepository.save(cookieRecipe)
        recipeRepository.save(cakeRecipe)
    }

    @Test
    fun `should find recipes by category`() {
        val cookieRecipes = recipeRepository.findByCategory(RecipeCategory.COOKIES)
        val cakeRecipes = recipeRepository.findByCategory(RecipeCategory.CAKE)

        assertThat(cookieRecipes).hasSize(1)
        assertThat(cookieRecipes.first().name).isEqualTo("Chocolate Chip Cookies")

        assertThat(cakeRecipes).hasSize(1)
        assertThat(cakeRecipes.first().name).isEqualTo("Vanilla Cake")
    }

    @Test
    fun `should find recipes by name or description containing text`() {
        val chocolateRecipes = recipeRepository.findByNameContainingOrDescriptionContaining("chocolate")
        val vanillaRecipes = recipeRepository.findByNameContainingOrDescriptionContaining("vanilla")

        assertThat(chocolateRecipes).hasSize(1)
        assertThat(chocolateRecipes.first().name).isEqualTo("Chocolate Chip Cookies")

        assertThat(vanillaRecipes).hasSize(1)
        assertThat(vanillaRecipes.first().name).isEqualTo("Vanilla Cake")
    }

    @Test
    fun `should find recipes within time limit`() {
        val quickRecipes = recipeRepository.findByTotalTimeLimit(30)
        val allRecipes = recipeRepository.findByTotalTimeLimit(100)

        assertThat(quickRecipes).hasSize(1)
        assertThat(quickRecipes.first().name).isEqualTo("Chocolate Chip Cookies")

        assertThat(allRecipes).hasSize(2)
    }

    @Test
    fun `should find recipes with specific ingredients`() {
        val recipesWithFlour = recipeRepository.findRecipesWithAnyIngredient(listOf("Flour"))
        val recipesWithAllIngredients = recipeRepository.findRecipesWithAllIngredients(listOf("Flour", "Sugar"))

        assertThat(recipesWithFlour).hasSize(2)
        assertThat(recipesWithAllIngredients).hasSize(2)
    }

    @Test
    fun `should find recipes by difficulty`() {
        val easyRecipes = recipeRepository.findByDifficulty("EASY")
        val mediumRecipes = recipeRepository.findByDifficulty("MEDIUM")

        assertThat(easyRecipes).hasSize(1)
        assertThat(easyRecipes.first().name).isEqualTo("Chocolate Chip Cookies")

        assertThat(mediumRecipes).hasSize(1)
        assertThat(mediumRecipes.first().name).isEqualTo("Vanilla Cake")
    }
}