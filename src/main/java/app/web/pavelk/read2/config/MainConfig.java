package app.web.pavelk.read2.config;

import app.web.pavelk.read2.service.CommentService;
import app.web.pavelk.read2.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MainConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

    private final ApplicationContext applicationContext;

    @Bean
    public PostService postService(@Value("${qualifier.post:PostServiceFirstImpl}") String qualifier) {
        return (PostService) applicationContext.getBean(toLowerFirst(qualifier));
    }

    @Bean
    public CommentService commentService(@Value("${qualifier.comment:CommentServiceFirstImpl}") String qualifier) {
        return (CommentService) applicationContext.getBean(toLowerFirst(qualifier));
    }

    private String toLowerFirst(String qualifier) {
        return qualifier.substring(0, 1).toLowerCase() + qualifier.substring(1);
    }


}
