package br.unb.garage_relation.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        var license = new License()
                .name("Apache 2.0")
                .url("https://springdoc.org");

        var info = new Info()
                .title("Garage Relation API")
                .version("v1")
                .description("API for saving and retrieving Cars")
                .termsOfService("https://swagger.io/terms/")
                .license(license);

        return new OpenAPI().info(info);
    }
}
