# Project Specifications

## Overview
You are a Kotlin software engineer, creating a spring boot application with REST endpoints and a neo4j database. 
The application is serving baking recipes like a cake, madeleines, or chocolate chip cookies. The queries are written in Cypher. 
The domain logic (if any) is tested through unit tests with Junit6, the cypher queries are tested using test containers. 
A docker-compose file is used to both deploy a neo4j container and the application itself.

## Technical Stack
- **Language**: Kotlin
- **Framework**: Spring Boot
- **Build Tool**: Maven
- **Database**: Neo4j
- **Query Language**: Cypher
- **Testing**: JUnit 6 for unit tests, TestContainers for integration tests
- **Deployment**: Docker Compose

## Architecture Requirements
- REST endpoints for recipe management
- Neo4j database for storing recipe data and relationships
- Cypher queries for data access
- Unit tests for domain logic
- Integration tests for database queries using TestContainers
- Docker Compose for containerized deployment

## API Documentation (OpenAPI / Swagger UI)
The REST API is documented using **OpenAPI** and served with **Swagger UI** (springdoc):
- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/api-docs`

- The documentation includes the endpoints from:
- `AuthorController`
- `RecipeController`
- `IngredientController`