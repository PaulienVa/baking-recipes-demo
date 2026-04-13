package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.Ingredient
import com.openvalue.bakingrecipes.domain.Recipe
import com.openvalue.bakingrecipes.repository.projections.MostUsedIngredientRow
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface IngredientRepository : Neo4jRepository<Ingredient, Long> {

    fun findByNameContainingIgnoreCase(name: String): List<Ingredient>

    @Query("""
        MATCH (i:Ingredient)<-[:REQUIRES]-(r:Recipe)
        RETURN i AS ingredient, COUNT(r) AS recipeCount
        ORDER BY recipeCount DESC
        LIMIT 10
    """)
    fun findMostUsedIngredients(): List<MostUsedIngredientRow>
}