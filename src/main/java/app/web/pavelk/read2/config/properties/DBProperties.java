package app.web.pavelk.read2.config.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.first")
public class DBProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
