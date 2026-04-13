package com.openvalue.bakingrecipes.api

import com.openvalue.bakingrecipes.service.AuthorService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException


@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService
) {

    @GetMapping
    fun retrieveAuthors(): List<AuthorOfRecipe> {
        return authorService.findAllAuthors().toList()
    }

    @GetMapping("/{name}")
    fun retrieveAuthorByName(@PathVariable name: String): AuthorOfRecipe {
        val found =
            authorService.findByNameContainingIgnoreCase(name)
        return found
    }
}