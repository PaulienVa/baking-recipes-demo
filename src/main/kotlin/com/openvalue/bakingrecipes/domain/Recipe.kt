package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
data class Recipe(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String,
    val description: String,
    val preparationTime: Int,
    val cookingTime: Int,
    val servings: Int,
    val difficulty: Difficulty,
    val category: RecipeCategory,

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    val ingredients: Set<Ingredient> = emptySet(),

    @Relationship(type = "HAS_STEP", direction = Relationship.Direction.OUTGOING)
    val steps: List<Step> = emptyList()
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}

enum class RecipeCategory {
    CAKE, MADELEINES, COOKIES
}