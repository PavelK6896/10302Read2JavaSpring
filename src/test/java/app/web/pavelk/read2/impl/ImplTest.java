package app.web.pavelk.read2.impl;

import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.service.impl.PostServiceFirstImpl;
import app.web.pavelk.read2.service.impl.PostServiceMapImpl;
import app.web.pavelk.read2.service.impl.PostServiceQueryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = {ImplTest.Initializer.class})
@AutoConfigureMockMvc(addFilters = false)
class ImplTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostServiceFirstImpl postServiceFirst;
    @Autowired
    private PostServiceMapImpl postServiceMap;
    @Autowired
    private PostServiceQueryImpl postServiceQuery;

    @Container
    public static GenericContainer<?> postgres = new GenericContainer<>(DockerImageName.parse("postgres:14"))
            .withExposedPorts(5432)
            .withEnv("POSTGRES_USER", "postgres")
            .withEnv("POSTGRES_PASSWORD", "postgres")
            .withEnv("POSTGRES_DB", "read2");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.driver-class-name=org.postgresql.Driver",
                    "spring.datasource.url=" + "jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/read2",
                    "spring.datasource.username=postgres",
                    "spring.datasource.password=postgres",
                    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL95Dialect"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    void getAllPosts() throws Exception {

        ResponseEntity<Page<PostResponseDto>> response1 = postServiceFirst.getAllPosts(PageRequest.of(0, 10, Sort.unsorted()));
        ResponseEntity<Page<PostResponseDto>> response2 = postServiceMap.getAllPosts(PageRequest.of(0, 10, Sort.unsorted()));
        ResponseEntity<Page<PostResponseDto>> response3 = postServiceQuery.getAllPosts(PageRequest.of(0, 10, Sort.unsorted()));

        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);

        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

}
