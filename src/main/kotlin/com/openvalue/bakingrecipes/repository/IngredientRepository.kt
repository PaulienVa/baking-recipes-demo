package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.domain.Recipe
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface IngredientRepository : Neo4jRepository<Ingredient, Long> {

    fun findByNameContainingIgnoreCase(name: String): List<Ingredient>

//    fun findByType(type: IngredientType): List<Ingredient>

    @Query("""
        MATCH (i:Ingredient)<-[:CONTAINS]-(r:Recipe)
        WHERE i.name = ${'$'}ingredientName
        RETURN r
    """)
    fun findRecipesUsingIngredient(@Param("ingredientName") ingredientName: String): List<Recipe>

    @Query("""
        MATCH (i:Ingredient)<-[:CONTAINS]-(r:Recipe)
        RETURN i.name as ingredient, COUNT(r) as recipeCount
        ORDER BY recipeCount DESC
        LIMIT 10
    """)
    fun findMostUsedIngredients(): List<Map<String, Int>>
}