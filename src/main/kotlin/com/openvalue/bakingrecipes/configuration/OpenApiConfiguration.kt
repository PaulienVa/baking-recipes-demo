package com.openvalue.bakingrecipes.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Baking Recipes API",
        version = "v1",
        description = "REST API for authors, recipes and ingredients."
    )
)
class OpenApiConfig