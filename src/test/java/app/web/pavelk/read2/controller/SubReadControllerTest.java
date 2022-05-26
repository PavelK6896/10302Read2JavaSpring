package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.Read2;
import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.service.MailService;
import app.web.pavelk.read2.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@SpringBootTest(classes = Read2.class)
@AutoConfigureMockMvc(addFilters = false)
class SubReadControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SubReadRepository subReadRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private MailService mailService;

    @BeforeEach
    private void ClearBase() {

        voteRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
        subReadRepository.deleteAll();
        verificationTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        userDetailsService.getUserMap().clear();

    }

    @Test
    void createSubreddit1Right() throws Exception {
        Long id = 1L;
        String name = "createSubreddit1RightN";
        String description = "createSubreddit1RightD";
        SubReadDto subReadDto = SubReadDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .numberOfPosts(1)
                .build();
        mockMvc.perform(post("/api/subreddit")
                        .content(objectMapper.writeValueAsString(subReadDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));
    }

    @Test
    void createSubreddit2Wrong() throws Exception {
        Long id = 1L;
        String description = "createSubreddit2WrongD";
        SubReadDto subReadDto = SubReadDto.builder()
                .id(id)
                .name(null)
                .description(description)
                .numberOfPosts(1)
                .build();
        mockMvc.perform(post("/api/subreddit")
                        .content(objectMapper.writeValueAsString(subReadDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("name is marked non-null but is null"));
    }


    @Test
    void getAllSubreddits1Right() throws Exception {
        String name1 = "getAllSubreddits1Right1";
        String name2 = "getAllSubreddits1Right2";
        String description = "getAllSubreddits1RightD";
        SubRead subRead1 = subReadRepository.save(SubRead.builder()
                .name(name1)
                .description(description)
                .build());
        SubRead subRead2 = subReadRepository.save(SubRead.builder()
                .name(name2)
                .description(description)
                .build());
        mockMvc.perform(get("/api/subreddit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(name1)))
                .andExpect(jsonPath("$[1].name", is(name2)))
                .andExpect(jsonPath("$[0].description", is(description)))
                .andExpect(jsonPath("$[1].description", is(description)))
                .andExpect(jsonPath("$[0].id", is(subRead1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(subRead2.getId().intValue())));
    }

    @Test
    void getAllSubreddits2Wrong() throws Exception {
        mockMvc.perform(get("/api/subreddit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getSubredditRight1() throws Exception {
        String name1 = "getSubreddit1Right";
        String description = "getSubreddit1RightD";
        SubRead subRead1 = subReadRepository.save(SubRead.builder()
                .name(name1)
                .description(description)
                .build());
        mockMvc.perform(get("/api/subreddit/" + subRead1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name1)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.id", is(subRead1.getId().intValue())));
    }

    @Test
    void getSubredditWrong2() throws Exception {
        long id = 2626L;
        mockMvc.perform(get("/api/subreddit/" + id))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("No subreddit found with ID - " + id));
    }

}
