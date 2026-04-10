package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.*

@Node
data class Utenstil(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String,
    @Relationship(type = "IS_USED_IN", direction = Relationship.Direction.INCOMING)
    val recipes: List<RecipeUtensil> = emptyList()
)

@RelationshipProperties
data class RecipeUtensil(
    @RelationshipId
    val id: Long,
    val quantity: Double,
    val unit: String?,

    @TargetNode
    val recipe: Recipe
)