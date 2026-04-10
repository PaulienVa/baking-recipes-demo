package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.*
import java.time.Duration

@Node
data class Step(
    @Id @GeneratedValue
    val id: Long? = null,
    val instruction: String,
    val estimatedTime: Duration?,
    val waitingTime: Duration?,
)

@RelationshipProperties
data class StepInRecipe(
    @RelationshipId
    val id: Long,
    val order: Int,

    @TargetNode
    val step: Step,
)