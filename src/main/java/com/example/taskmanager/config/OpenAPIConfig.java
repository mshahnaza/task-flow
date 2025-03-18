package com.example.taskmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("API for managing tasks, comments, and categories.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shakhnaza Mamirova")
                                .email("shakhnaza.mamirova@alatoo.edu.kg")
                                .url("https://github.com/mshahnaza")));
    }
}
