package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.LoginRequest;
import app.web.pavelk.read2.dto.RefreshTokenRequest;
import app.web.pavelk.read2.dto.RegisterRequest;
import app.web.pavelk.read2.schema.RefreshToken;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VerificationToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.UUID;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.REFRESH_TOKEN_NOT_FOUND;
import static app.web.pavelk.read2.util.StaticField.REFRESH_TOKEN_DELETED;
import static app.web.pavelk.read2.util.StaticField.USER_REGISTRATION_SUCCESSFUL;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DirtiesContext
class AuthControllerTest extends TestCommonController {


    @Test
    void signUp_CreateNewUser_Right() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .password(password)
                .username(username).build();
        mockMvc.perform(post("/auth/sign-up")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(USER_REGISTRATION_SUCCESSFUL));
    }

    @Test
    void signUp_CreateNewUser_Wrong() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email)
                .build();
        mockMvc.perform(post("/auth/sign-up")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void accountVerification_Right() throws Exception {
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
        mockMvc.perform(get("/auth/account-verification/4412ced7-1faf-49b4-a05a-d1cee3c526af"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(header().exists("Location"));
    }

    @Test
    void accountVerification_Wrong() throws Exception {
        mockMvc.perform(get("/auth/account-verification/ljljljljlkjlk"))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(content().string(REFRESH_TOKEN_NOT_FOUND.getMessage()));
    }

    @Test
    void loginAll_Right() throws Exception {
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
        mockMvc.perform(post("/auth/sign-in")
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
    void login_Password_Wrong() throws Exception {
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
        mockMvc.perform(post("/auth/sign-in")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(result -> assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(), result.getResponse().getErrorMessage()));
    }

    @Test
    void refreshToken_Right() throws Exception {
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
        mockMvc.perform(post("/auth/refresh/token")
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
    void refreshToken_Wrong() throws Exception {
        String string = UUID.randomUUID().toString();
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(string)
                .username(username)
                .build();
        mockMvc.perform(post("/auth/refresh/token")
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
        mockMvc.perform(post("/auth/sign-out")
                        .content(objectMapper.writeValueAsString(refreshTokenRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().string(REFRESH_TOKEN_DELETED));
        Assertions.assertThat(refreshTokenRepository.findByToken(string)).isEmpty();
    }
}
