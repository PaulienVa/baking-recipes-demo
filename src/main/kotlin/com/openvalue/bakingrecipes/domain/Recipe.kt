package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import kotlin.time.Duration

@Node
data class Recipe(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String,
    val description: String,
    val preparationTime: Duration,
    val waitingTime: Duration?,
    val cookingTime: Duration,
    val servings: Int,
    val difficulty: Difficulty,
    val category: RecipeCategory,

    @Relationship(type = "REQUIRES", direction = Relationship.Direction.OUTGOING)
    val requiredIngredients: List<QuantifiedIngredient> = emptyList(),

    @Relationship(type = "COULD_ALSO_CONTAIN", direction = Relationship.Direction.OUTGOING)
    val optionalIngredients: List<QuantifiedIngredient> = emptyList(),

    @Relationship(type = "IS_PREPARED_BY", direction = Relationship.Direction.OUTGOING)
    val steps: List<StepInRecipe> = emptyList()
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}

enum class RecipeCategory {
    BAKING, MAIN_COURSE, DESSERT, LUNCH
}