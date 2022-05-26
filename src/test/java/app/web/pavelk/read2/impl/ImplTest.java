package app.web.pavelk.read2.impl;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.repository.SubReadRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
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
    @Autowired
    private SubReadRepository subReadRepository;

    @Container
    public static GenericContainer<?> postgres = new GenericContainer<>(DockerImageName.parse("postgres:14")).withExposedPorts(5432).withEnv("POSTGRES_USER", "postgres").withEnv("POSTGRES_PASSWORD", "postgres").withEnv("POSTGRES_DB", "read2");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.driver-class-name=org.postgresql.Driver", "spring.datasource.url=" + "jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/read2", "spring.datasource.username=postgres", "spring.datasource.password=postgres", "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL95Dialect").applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    void getAllPosts() throws Exception {

        PageRequest of = PageRequest.of(0, 10, Sort.unsorted());
        ResponseEntity<Page<PostResponseDto>> response1 = postServiceFirst.getPagePosts(of);
        ResponseEntity<Page<PostResponseDto>> response2 = postServiceMap.getPagePosts(of);
        ResponseEntity<Page<PostResponseDto>> response3 = postServiceQuery.getPagePosts(of);

        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);

        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

    @Test
    @WithMockUser(username = "admin")
    void createPost() throws Exception {
        PostRequestDto postRequestDto = PostRequestDto.builder().description("op").postName("name-1").subReadName("Technical").build();
        ResponseEntity<Void> response1 = postServiceFirst.createPost(postRequestDto);
        ResponseEntity<Void> response2 = postServiceMap.createPost(postRequestDto);
        ResponseEntity<Void> response3 = postServiceQuery.createPost(postRequestDto);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

    @Test
    void getPost() throws Exception {
        ResponseEntity<PostResponseDto> response1 = postServiceFirst.getPost(1L);
        ResponseEntity<PostResponseDto> response2 = postServiceMap.getPost(1L);
        ResponseEntity<PostResponseDto> response3 = postServiceQuery.getPost(1L);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

    @Test
    void getPostsBySubreddit() throws Exception {
        PageRequest of = PageRequest.of(0, 10, Sort.unsorted());

        ResponseEntity<Page<PostResponseDto>> response1 = postServiceFirst.getPagePostsBySubreddit(1L, of);
        ResponseEntity<Page<PostResponseDto>> response2 = postServiceMap.getPagePostsBySubreddit(1L, of);
        ResponseEntity<Page<PostResponseDto>> response3 = postServiceQuery.getPagePostsBySubreddit(1L, of);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

    @Test
    void getPostsByUsername() throws Exception {
        PageRequest of = PageRequest.of(0, 10, Sort.unsorted());
        final String NAME = "Pavel";
        ResponseEntity<Page<PostResponseDto>> response1 = postServiceFirst.getPagePostsByUsername(NAME, of);
        ResponseEntity<Page<PostResponseDto>> response2 = postServiceFirst.getPagePostsByUsername(NAME, of);
        ResponseEntity<Page<PostResponseDto>> response3 = postServiceQuery.getPagePostsByUsername(NAME, of);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

}
