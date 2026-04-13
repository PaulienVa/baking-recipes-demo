package com.openvalue.bakingrecipes.repository

import com.openvalue.bakingrecipes.domain.Author
import org.springframework.data.neo4j.repository.Neo4jRepository

interface AuthorRepository : Neo4jRepository<Author, Long> {
    fun findByNameContainingIgnoreCase(name: String): Author?
}