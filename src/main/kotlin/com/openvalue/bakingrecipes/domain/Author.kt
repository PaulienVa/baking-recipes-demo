package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.*

@Node
data class Author(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String,
    val website: String?,

    @Relationship(type = "HAS_WRITTEN", direction = Relationship.Direction.OUTGOING)
    val recipes: List<HasWritten> = emptyList()
)

@RelationshipProperties
data class HasWritten(

    @RelationshipId
    val id: Long? = null,
    val publishedIn: String,
    val publicationType: PublicationType,

    @TargetNode
    val recipe: Recipe
)

enum class PublicationType {
    WEBSITE, BOOK, MAGAZINE
}