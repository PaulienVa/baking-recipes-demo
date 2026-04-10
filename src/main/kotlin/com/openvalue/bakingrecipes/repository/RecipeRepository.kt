package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.domain.RecipeCategory
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : Neo4jRepository<Recipe, Long> {

    @Query("MATCH (r:Recipe) WHERE r.category = \"$\"category RETURN r")
    fun findByCategory(@Param("category") category: RecipeCategory): List<Recipe>

    @Query("""
        MATCH (r:Recipe)
        WHERE r.name CONTAINS {{"$"}}name OR r.description CONTAINS {{"$"}}name
        RETURN r
    """)
    fun findByNameContainingOrDescriptionContaining(@Param("name") name: String): List<Recipe>

    @Query("""
        MATCH (r:Recipe)
        WHERE r.preparationTime + r.cookingTime <= {{"$"}}maxTime
        RETURN r ORDER BY r.preparationTime + r.cookingTime ASC
    """)
    fun findByTotalTimeLimit(@Param("maxTime") maxTime: Int): List<Recipe>

    @Query("""
        MATCH (r:Recipe)-[:CONTAINS]->(i:Ingredient)
        WHERE i.name IN {{"$"}}ingredients
        WITH r, COUNT(DISTINCT i) as ingredientCount
        WHERE ingredientCount = SIZE({{"$"}}ingredients)
        RETURN r
    """)
    fun findRecipesWithAllIngredients(@Param("ingredients") ingredients: List<String>): List<Recipe>

    @Query("""
        MATCH (r:Recipe)-[:CONTAINS]->(i:Ingredient)
        WHERE i.name IN ${'$'}ingredients
        RETURN DISTINCT r
    """)
    fun findRecipesWithAnyIngredient(@Param("ingredients") ingredients: List<String>): List<Recipe>

    @Query("""
        MATCH (r:Recipe)
        WHERE r.difficulty =  ${'$'}difficulty
        RETURN r ORDER BY r.name ASC
    """)
    fun findByDifficulty(@Param("difficulty") difficulty: String): List<Recipe>
}