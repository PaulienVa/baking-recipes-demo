package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@Node
data class Ingredient(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String,
    val type: IngredientType
)

@RelationshipProperties
data class IngredientQuantity(
    val quantity: Double,
    val unit: String,

    @TargetNode
    val ingredient: Ingredient
)

enum class IngredientType {
    FLOUR, SUGAR, EGGS, BUTTER, MILK, CHOCOLATE, VANILLA, BAKING_POWDER, SALT, OTHER
}