package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.time.Duration

@Node
data class Recipe(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String,
    val description: String,
    val preparationTime: Duration,
    val cookingTime: Duration,
    val servings: Int,

    @Relationship(type = "REQUIRES", direction = Relationship.Direction.OUTGOING)
    val requiredIngredients: List<QuantifiedIngredient> = emptyList(),

    @Relationship(type = "COULD_ALSO_CONTAIN", direction = Relationship.Direction.OUTGOING)
    val optionalIngredients: List<QuantifiedIngredient> = emptyList(),

    @Relationship(type = "IS_PREPARED_BY", direction = Relationship.Direction.OUTGOING)
    val steps: List<StepInRecipe> = emptyList()
)