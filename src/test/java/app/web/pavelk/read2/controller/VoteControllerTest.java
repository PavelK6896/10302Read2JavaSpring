package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.Read2;
import app.web.pavelk.read2.dto.VoteDto;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VoteType;
import app.web.pavelk.read2.service.MailService;
import app.web.pavelk.read2.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("")
@ActiveProfiles("dev")
@SpringBootTest(classes = Read2.class)
@AutoConfigureMockMvc(addFilters = false)
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
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

    final String username1 = "voteRight1";

    @Test
    @WithMockUser(username = username1)
    void voteRight1() throws Exception {
        String name = "voteRight1N";
        String description = "voteRight1D";
        String password1 = "dsd$%#@sdfs";
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password1))
                .enabled(true)
                .build());
        SubRead subRead = subReadRepository.save(SubRead.builder()
                .description(description)
                .name(name)
                .user(user)
                .build());
        Post post1 = postRepository.save(Post.builder()
                .createdDate(LocalDateTime.now())
                .postName("post1")
                .user(user)
                .description("d1")
                .voteCount(10)
                .subRead(subRead)
                .build());
        VoteDto voteDto = VoteDto.builder()
                .voteType(VoteType.UP_VOTE)
                .postId(post1.getId())
                .build();
        mockMvc.perform(post("/api/votes")
                        .content(objectMapper.writeValueAsString(voteDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(202));

    }

    @Test
    @WithMockUser(username = username1)
    void voteWrong2() throws Exception {
        String password1 = "dsd$%#@sdfs";
        userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password1))
                .enabled(true)
                .build());
        VoteDto voteDto = VoteDto.builder()
                .voteType(VoteType.DOWN_VOTE)
                .postId(150L)
                .build();
        mockMvc.perform(post("/api/votes")
                        .content(objectMapper.writeValueAsString(voteDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));
    }
}
