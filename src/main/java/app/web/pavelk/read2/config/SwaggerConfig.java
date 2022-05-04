package app.web.pavelk.read2.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.annotation.PostConstruct;

@Slf4j(topic = "swagger-config")
@Configuration
public class SwaggerConfig {

    public static final String BEARER = "Bearer";
    public static final String READ_2 = "Read 2";

    @PostConstruct
    public void pathsLog() {
        log.info("http://localhost:8080/api/read2/swagger-ui/index.html");
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder().group(READ_2).pathsToMatch("/**").build();
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
                                        .name(HttpHeaders.AUTHORIZATION)
                        ))
                .info(new io.swagger.v3.oas.models.info.Info().title(READ_2).version("1"));
    }

}
