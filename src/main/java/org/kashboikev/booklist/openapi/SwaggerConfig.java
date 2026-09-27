package org.kashboikev.booklist.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().
                info(
                        new Info()
                                .title("Book API")
                                .version("1.0")
                                .description(
                                        "Api for book management system"
                                )
                );
    }

}
