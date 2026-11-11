package br.com.fiap3esph.trajetoria.config.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(
                                "bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .info(new Info()
                        .title("TRAJEtória")
                        .description("API REST da plataforma TRAJEtória: doação de roupas sociais para jovens em vulnerabilidade, indicados por ONGs parceiras. Projeto da Global Solution CitizenTech - FIAP 2026.")
                        .contact(new Contact()
                                .name("Goddess Tech")
                                .email("trajetoria@email.com.br")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")//Endereço de exemplo
                        )
                );
    }
}
