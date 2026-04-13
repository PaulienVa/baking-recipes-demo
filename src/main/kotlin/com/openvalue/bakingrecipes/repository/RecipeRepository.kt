package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.Recipe
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : Neo4jRepository<Recipe, Long> {

    @Query("""
        MATCH (r:Recipe)
        WHERE r.name CONTAINS ${'$'}name OR r.description CONTAINS ${'$'}name
        RETURN r
    """)
    fun findByNameContainingOrDescriptionContaining(@Param("name") name: String): List<Recipe>
}