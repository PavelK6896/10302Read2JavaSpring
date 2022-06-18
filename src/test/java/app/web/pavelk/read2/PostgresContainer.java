package app.web.pavelk.read2;


import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@Testcontainers
@ContextConfiguration(initializers = {PostgresContainer.Initializer.class})
public abstract class PostgresContainer {

    @Container
    static final GenericContainer<?> postgres = new GenericContainer<>(DockerImageName
            .parse("postgres:14"))
            .withExposedPorts(5432)
            .withEnv("POSTGRES_USER", "postgres")
            .withEnv("POSTGRES_PASSWORD", "postgres")
            .withEnv("POSTGRES_DB", "read2");

    static final class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.driver-class-name=org.postgresql.Driver",
                            "spring.datasource.url=" + "jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/read2",
                            "spring.datasource.username=postgres",
                            "spring.datasource.password=postgres",
                            "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL95Dialect")
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
