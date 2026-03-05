package com.openvalue.bakingrecipes.domain

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Step(
    @Id @GeneratedValue
    val id: Long? = null,
    val stepNumber: Int,
    val instruction: String,
    val estimatedTime: Int? = null
)