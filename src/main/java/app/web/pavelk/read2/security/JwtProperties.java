package app.web.pavelk.read2.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Time duration token.
     */
    private String expiration = "150";

}
