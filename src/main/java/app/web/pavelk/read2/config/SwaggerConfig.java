package app.web.pavelk.read2.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String BEARER = "Bearer";

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder().group("Read 2").pathsToMatch("/**").build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(BEARER))
                .components(new Components()
                        .addSecuritySchemes(BEARER,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme(BEARER)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        ))
                .info(new io.swagger.v3.oas.models.info.Info().title("Read 2").version("1"));
    }

}
