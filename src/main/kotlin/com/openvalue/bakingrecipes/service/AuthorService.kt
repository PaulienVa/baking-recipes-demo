package com.openvalue.bakingrecipes.service

import com.openvalue.bakingrecipes.api.domain.AuthorOfRecipe
import com.openvalue.bakingrecipes.domain.Author
import com.openvalue.bakingrecipes.repository.AuthorRepository
import org.springframework.stereotype.Service

@Service
class AuthorService(private val authorRepository: AuthorRepository) {
    fun saveAuthor(author: AuthorOfRecipe) {
        with(author) {
            val retrievedAuthor = authorRepository.findByNameContainingIgnoreCase(name)
            retrievedAuthor ?: authorRepository.save(Author(name = name, website = website))
        }
    }

    fun findByNameContainingIgnoreCase(name: String): AuthorOfRecipe {
        val found = authorRepository.findByNameContainingIgnoreCase(name) ?: throw IllegalArgumentException("$name not found")
        return AuthorOfRecipe(name = found.name, website = found.website)
    }

    fun findAllAuthors(): Iterable<AuthorOfRecipe> {
        return authorRepository.findAll().map { toAuthorOfRecipe(it)  }
    }

    fun toAuthorOfRecipe(author: Author): AuthorOfRecipe {
        return with(author) {
            AuthorOfRecipe(name = name, website = website)
        }
    }
}
