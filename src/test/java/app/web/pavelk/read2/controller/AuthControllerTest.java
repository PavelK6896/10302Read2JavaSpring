package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.Read2;
import app.web.pavelk.read2.dto.LoginRequest;
import app.web.pavelk.read2.dto.RefreshTokenRequest;
import app.web.pavelk.read2.dto.RegisterRequest;
import app.web.pavelk.read2.repository.*;
import app.web.pavelk.read2.schema.RefreshToken;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VerificationToken;
import app.web.pavelk.read2.service.MailService;
import app.web.pavelk.read2.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("dev")
@SpringBootTest(classes = Read2.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    private SubredditRepository subredditRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private MailService mailService;

    @BeforeEach
    @Transactional
    public void clearBase() {

        voteRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
        subredditRepository.deleteAll();
        verificationTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        userDetailsService.getUserMap().clear();

    }

    static String username = "usernameTest";
    static String password = "as!@AS123Test";
    static String email = "sa837@test.com";

    @Test
    void signUp1Right() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .password(password)
                .username(username).build();
        mockMvc.perform(post("/api/auth/signUp")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("User registration successful."));
    }

    @Test
    void signUp2Wrong() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .build();
        mockMvc.perform(post("/api/auth/signUp")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void accountVerification1Right() throws Exception {
        User user = userRepository.save(User.builder()
                .id(1L)
                .created(Instant.now())
                .email(email)
                .username(username)
                .password(password)
                .build());
        verificationTokenRepository.save(VerificationToken.builder()
                .id(1L)
                .token("4412ced7-1faf-49b4-a05a-d1cee3c526af")
                .user(user)
                .build());
        mockMvc.perform(get("/api/auth/accountVerification/4412ced7-1faf-49b4-a05a-d1cee3c526af"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(header().exists("Location"));
    }

    @Test
    void accountVerification2Wrong() throws Exception {
        mockMvc.perform(get("/api/auth/accountVerification/ljljljljlkjlk"))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(content().string("Invalid verification Token"));
    }

    @Test
    void login1AllRight() throws Exception {
        userRepository.save(User.builder()
                .created(Instant.now())
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build());
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.authenticationToken").exists())
                .andExpect(jsonPath("$.authenticationToken").isString())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    void login2WrongPassword() throws Exception {
        userRepository.save(User.builder()
                .created(Instant.now())
                .email(email)
                .username(username)
                .password(passwordEncoder.encode("test"))
                .enabled(true)
                .build());
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(result -> assertEquals("Bad credentials", result.getResponse().getErrorMessage()));
    }

    @Test
    void login3WrongPassword() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(content().string("No user Found with username : " + username));
    }

    @Test
    void refreshToken1Right() throws Exception {
        String string = UUID.randomUUID().toString();
        userRepository.save(User.builder()
                .created(Instant.now())
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build());
        refreshTokenRepository.save(RefreshToken.builder()
                .createdDate(Instant.now())
                .token(string)
                .build());
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(string)
                .username(username)
                .build();
        mockMvc.perform(post("/api/auth/refresh/token")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.authenticationToken").exists())
                .andExpect(jsonPath("$.authenticationToken").isString())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    void refreshToken2Wrong() throws Exception {
        String string = UUID.randomUUID().toString();
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(string)
                .username(username)
                .build();
        mockMvc.perform(post("/api/auth/refresh/token")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(content().string("Invalid refresh Token"));
    }


    @Test
    void logout1Right() throws Exception {
        String string = UUID.randomUUID().toString();
        refreshTokenRepository.save(RefreshToken.builder()
                .createdDate(Instant.now())
                .token(string)
                .build());
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(string)
                .username(username)
                .build();
        mockMvc.perform(post("/api/auth/logout")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string("Refresh Token Deleted Successfully!"));
        Assertions.assertThat(refreshTokenRepository.findByToken(string)).isEmpty();
    }
}
