package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.Author
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : Neo4jRepository<Author, Long> {

    fun findByNameContainingIgnoreCase(name: String): Author?

    @Query("""
        MATCH (a:Author)-[:HAS_WRITTEN]->(r :Recipe)
        WHERE r.name = ${'$'}name
        RETURN a.name
    """)
    fun findAuthorOfRecipe(@Param("name") name: String) : String

    @Query("""
        MATCH (a:Author)-[:HAS_WRITTEN]->(:Recipe)-[r:REQUIRES]->(:Ingredient)
        RETURN a as authors, count(r) as ingredientCount
        ORDER BY ingredientCount LIMIT 1;
    """)
    fun findMostComplexRecipe() : Author
}