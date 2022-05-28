package app.web.pavelk.read2.config;

import app.web.pavelk.read2.service.CommentService;
import app.web.pavelk.read2.service.PostService;
import app.web.pavelk.read2.service.SubReadService;
import app.web.pavelk.read2.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableAsync
@ConfigurationPropertiesScan
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

    @Value("${qualifier.allPostfix:First}")
    String allPostfix;

    private String toLowerFirst(String qualifier) {
        return qualifier.substring(0, 1).toLowerCase() + qualifier.substring(1);
    }

    private String checkQualifierEmpty(Class<?> classType, String qualifier) {
        if (qualifier.isEmpty()) {
            qualifier = classType.getName().replace(classType.getPackageName() + ".", "") + allPostfix + "Impl";
        }
        return qualifier;
    }

    @Bean
    public PostService postService(@Value("${qualifier.post:}") String qualifier) {
        return (PostService) applicationContext.getBean(toLowerFirst(checkQualifierEmpty(PostService.class, qualifier)));
    }

    @Bean
    public CommentService commentService(@Value("${qualifier.comment:}") String qualifier) {
        return (CommentService) applicationContext.getBean(toLowerFirst(checkQualifierEmpty(CommentService.class, qualifier)));
    }

    @Bean
    public SubReadService subReadService(@Value("${qualifier.sub:}") String qualifier) {
        return (SubReadService) applicationContext.getBean(toLowerFirst(checkQualifierEmpty(SubReadService.class, qualifier)));
    }

    @Bean
    public VoteService voteService(@Value("${qualifier.vote:}") String qualifier) {
        return (VoteService) applicationContext.getBean(toLowerFirst(checkQualifierEmpty(VoteService.class, qualifier)));
    }

}
