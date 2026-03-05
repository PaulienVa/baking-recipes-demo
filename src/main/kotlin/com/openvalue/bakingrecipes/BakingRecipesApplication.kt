package com.openvalue.bakingrecipes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BakingRecipesApplication

fun main(args: Array<String>) {
    runApplication<BakingRecipesApplication>(*args)
}