package app.web.pavelk.read2.config;


import app.web.pavelk.read2.config.properties.DBProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DBConfig {

    private final DBProperties dbProperties;

    @Bean
    @Primary
    public DataSourceProperties firstDataSourceProperties() throws URISyntaxException {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            dataSourceProperties.setDriverClassName(dbProperties.getDriverClassName());
            dataSourceProperties.setUrl(dbProperties.getUrl());
            dataSourceProperties.setPassword(dbProperties.getPassword());
            dataSourceProperties.setUsername(dbProperties.getUsername());
            return dataSourceProperties;
        }

        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        dataSourceProperties.setDriverClassName(dbProperties.getDriverClassName());
        dataSourceProperties.setUrl(dbUrl);
        dataSourceProperties.setPassword(password);
        dataSourceProperties.setUsername(username);
        return dataSourceProperties;
    }

}
