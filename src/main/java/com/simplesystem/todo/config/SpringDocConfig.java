package com.simplesystem.todo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openApiSpecs() {
        return new OpenAPI().info(new Info().title("TODO API")
                .description("This document specifies the API")
                .version("v1"));
    }
}
