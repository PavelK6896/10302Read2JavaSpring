package app.web.pavelk.read2.controller;


import app.web.pavelk.read2.Read2;
import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.Comment;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.Subreddit;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.service.MailService;
import app.web.pavelk.read2.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@SpringBootTest(classes = Read2.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentsControllerTest {

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
    private SubredditRepository subredditRepository;
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
        subredditRepository.deleteAll();
        verificationTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        userDetailsService.getUserMap().clear();

    }

    final static String username1 = "createComment1Right";
    final static String username2 = "createComment3WrongUsernameNotFoundException";
    static String password = "as!@AS123Test";

    @Test
    @WithMockUser(username = username1)
    void createComment1Right() throws Exception {
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build());
        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1")
                .name("name1")
                .user(user)
                .build());
        Post post = postRepository.save(Post.builder()
                .postId(1L)
                .postName("sz")
                .user(user)
                .description("11")
                .voteCount(0)
                .subreddit(subreddit)
                .build());
        CommentsDto commentsDto = CommentsDto.builder()
                .createdDate(Instant.now())
                .postId(post.getPostId())
                .text("comment1")
                .userName(username1)
                .build();

        mockMvc.perform(post("/api/comments/")
                        .content(objectMapper.writeValueAsString(commentsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));

    }

    @Test
    void createComment2WrongPostNotFoundException() throws Exception {
        CommentsDto commentsDto = CommentsDto.builder()
                .createdDate(Instant.now())
                .postId(2L)
                .text("comment1")
                .userName(username2)
                .build();

        mockMvc.perform(post("/api/comments/")
                        .content(objectMapper.writeValueAsString(commentsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("No post " + commentsDto.getPostId()));
    }

    @Test
    @WithMockUser(username = username2)
    void createComment3WrongUsernameNotFoundException() throws Exception {
        Subreddit subreddit = subredditRepository.save(Subreddit.builder()
                .description("d1")
                .name("name1")
                .user(null)
                .build());
        Post post = postRepository.save(Post.builder()
                .postId(1L)
                .postName("sz")
                .user(null)
                .description("11")
                .voteCount(0)
                .subreddit(subreddit)
                .build());
        CommentsDto comment1 = CommentsDto.builder()
                .createdDate(Instant.now())
                .postId(post.getPostId())
                .text("comment1")
                .userName(username2)
                .build();
        mockMvc.perform(post("/api/comments/")
                        .content(objectMapper.writeValueAsString(comment1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("User name not found " + username2));
    }


    @Test
    void getAllCommentsForPost1Right() throws Exception {
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build());
        Post post = postRepository.save(Post.builder()
                .postId(12L)
                .postName("s")
                .user(user)
                .description("11")
                .voteCount(0)
                .build());
        commentRepository.save(Comment.builder()
                .createdDate(Instant.now())
                .post(post)
                .user(user)
                .text("comment1")
                .build());
        commentRepository.save(Comment.builder()
                .createdDate(Instant.now())
                .post(post)
                .user(user)
                .text("comment2")
                .build());
        mockMvc.perform(get("/api/comments/by-post/" + post.getPostId()))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getAllCommentsForPost2Wrong() throws Exception {
        long postId = 2L;
        mockMvc.perform(get("/api/comments/by-post/" + postId))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("Not found post " + postId));
    }

    @Test
    void getAllCommentsForUser1Right() throws Exception {
        long postId = 14L;
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build());
        Post post = postRepository.save(Post.builder()
                .postId(postId)
                .postName("s")
                .user(user)
                .description("11")
                .voteCount(0)
                .build());
        commentRepository.save(Comment.builder()
                .createdDate(Instant.now())
                .post(post)
                .user(user)
                .text("comment1")
                .build());
        commentRepository.save(Comment.builder()
                .createdDate(Instant.now())
                .post(post)
                .user(user)
                .text("comment2")
                .build());
        mockMvc.perform(get("/api/comments/by-user/" + username1))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getAllCommentsForUser2Wrong() throws Exception {
        mockMvc.perform(get("/api/comments/by-user/" + username1))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("User name not found " + username1));
    }

}
