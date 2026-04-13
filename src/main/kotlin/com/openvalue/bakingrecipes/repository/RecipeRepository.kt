package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.domain.RecipeCategory
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : Neo4jRepository<Recipe, Long> {

    @Query("""
        MATCH (r:Recipe)
        WHERE r.name CONTAINS ${'$'}name OR r.description CONTAINS {{"$"}}name
        RETURN r
    """)
    fun findByNameContainingOrDescriptionContaining(@Param("name") name: String): List<Recipe>

    @Query("""
        MATCH (r:Recipe)
        WHERE r.preparationTime + r.cookingTime <= ${'$'}maxTime
        RETURN r ORDER BY r.preparationTime + r.cookingTime ASC
    """)
    fun findByTotalTimeLimit(@Param("maxTime") maxTime: Int): List<Recipe>

    @Query("""
        MATCH (i:Ingredient)<-[:REQUIRES]-(r:Recipe)
        WHERE i.name = ${'$'}ingredientName
        RETURN r
    """)
    fun findRecipesUsingIngredient(@Param("ingredientName") ingredientName: String): List<Recipe>


    @Query("""
        MATCH (r:Recipe)
        WHERE r.difficulty =  ${'$'}difficulty
        RETURN r ORDER BY r.name ASC
    """)
    fun findByDifficulty(@Param("difficulty") difficulty: String): List<Recipe>
}