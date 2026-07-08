package com.eab.openbread.web.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openBreadOpenAPI(): OpenAPI {
        val securitySchemeName = "BearerAuth"

        return OpenAPI()
            .info(
                Info()
                    .title("OpenBread API")
                    .version("1.0.0")
                    .description("OpenBread API docs.")
                    .contact(
                        Contact()
                            .name("Support")
                            .email("eduabenantec@hotmail.com")
                    )
            )
            .servers(
                listOf(
                    Server().url("http://localhost:8080").description("Localhost"),
                )
            )
            .components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .`in`(SecurityScheme.In.HEADER)
                        .description("JWT Token")
                )
            )
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
    }
}