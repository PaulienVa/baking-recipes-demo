package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.*

@Node
data class Ingredient(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String,
)

@RelationshipProperties
data class QuantifiedIngredient(
    @RelationshipId
    val id: Long? = null,
    val quantity: Double,
    val unit: String?,

    @TargetNode
    val ingredient: Ingredient
)