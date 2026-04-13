package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.Author
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : Neo4jRepository<Author, Long> {

    fun findByNameContainingIgnoreCase(name: String): Author?

    @Query("""
        MATCH (r:Recipe {name: ${'$'}name })-[:HAS_WRITTEN]->(a:Author))
        RETURN a.name
    """)
    fun findAuthorOfRecipe(name: String) : String
}