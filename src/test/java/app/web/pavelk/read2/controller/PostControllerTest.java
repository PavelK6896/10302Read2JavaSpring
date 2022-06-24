package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.schema.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.time.LocalDateTime;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.SUB_NOT_FOUND;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext
class PostControllerTest extends TestCommonController {

    @Test
    @WithMockUser(username = username1)
    void createNewPost_Right() throws Exception {
        String subredditName = "nameSub1";
        String password1 = "dsd$%#@sdfs";
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password1))
                .enabled(true)
                .build());
        subReadRepository.save(SubRead.builder()
                .description("d1")
                .name(subredditName)
                .user(user)
                .build());
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .description("op")
                .postName("name1")
                .subReadName(subredditName)
                .build();
        mockMvc.perform(post("/post")
                        .content(objectMapper.writeValueAsString(postRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));
    }

    @Test
    void createNewPost_Wrong() throws Exception {
        String subredditName = "nameSub2";
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .description("op")
                .postName("name1")
                .subReadName(subredditName)
                .build();
        mockMvc.perform(post("/post")
                        .content(objectMapper.writeValueAsString(postRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(content().string(SUB_NOT_FOUND.getMessage().formatted(subredditName)));
    }

    @Test
    void createPost_UserNotFound_Wrong() throws Exception {
        String subredditName = "nameSub2";
        SubRead subRead = subReadRepository.save(SubRead.builder()
                .description("d1")
                .name(subredditName)
                .user(null)
                .build());
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .description("op")
                .postName("name1")
                .subReadName(subredditName)
                .build();
        mockMvc.perform(post("/post")
                        .content(objectMapper.writeValueAsString(postRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(500));
        subReadRepository.delete(subRead);
    }

    @Test
    @WithMockUser(username = username2)
    void get_AllPosts_Right() throws Exception {
        String password1 = "dsd$%#@sdfs";
        Long postId1 = 104L;
        Long postId2 = 105L;
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username2)
                .password(passwordEncoder.encode(password1))
                .enabled(true)
                .build());
        SubRead subRead = subReadRepository.save(SubRead.builder()
                .description("d1")
                .name("name1")
                .user(user)
                .build());
        Post post1 = postRepository.save(Post.builder()
                .createdDate(LocalDateTime.now())
                .id(postId1)
                .postName("post1")
                .user(user)
                .description("d1")
                .voteCount(10)
                .subRead(subRead)
                .build());
        Post post2 = postRepository.save(Post.builder()
                .createdDate(LocalDateTime.now().minusDays(1))
                .id(postId2)
                .postName("post2")
                .user(user)
                .description("d2")
                .voteCount(20)
                .subRead(subRead)
                .build());
        mockMvc.perform(get("/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].postName", is("post1")))
                .andExpect(jsonPath("$.content[1].postName", is("post2")))
                .andExpect(jsonPath("$.content[0].subReadName", is("name1")))
                .andExpect(jsonPath("$.content[1].subReadName", is("name1")))
                .andExpect(jsonPath("$.content[0].id", is(post1.getId().intValue())))
                .andExpect(jsonPath("$.content[1].id", is(post2.getId().intValue())));
    }

    @Test
    @WithMockUser(username = username3)
    void get_PostById_Right() throws Exception {
        String password1 = "dsd$%#@sdfs";
        Long postId = 1L;
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username3)
                .password(passwordEncoder.encode(password1))
                .enabled(true)
                .build());
        SubRead subRead = subReadRepository.save(SubRead.builder()
                .description("d1")
                .name("subReadName1")
                .user(user)
                .build());
        Post post = postRepository.save(Post
                .builder()
                .createdDate(LocalDateTime.now())
                .id(postId)
                .postName("post")
                .user(user)
                .description("description1")
                .voteCount(20)
                .subRead(subRead)
                .build());
        mockMvc.perform(get("/post/" + post.getId().intValue()))
                .andDo(print())
                .andExpect(jsonPath("$.postName", is("post")))
                .andExpect(jsonPath("$.description", is("description1")))
                .andExpect(jsonPath("$.id", is(post.getId().intValue())))
                .andExpect(jsonPath("$.userName", is(username3)))
                .andExpect(jsonPath("$.subReadName", is("subReadName1")));

    }

    @Test
    @WithMockUser(username = username5)
    void get_PostsBySub_Right() throws Exception {

        String subredditName = "getPostsBySub1Right";
        String password1 = "dsd$%#@sdfs";
        Long postId = 1L;
        Long subredditId = 1L;
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username5)
                .password(passwordEncoder.encode(password1))
                .enabled(true)
                .build());
        SubRead subRead = subReadRepository.save(SubRead.builder()
                .id(subredditId)
                .description("d1")
                .name(subredditName)
                .user(user)
                .build());
        postRepository.save(Post.builder()
                .createdDate(LocalDateTime.now())
                .id(postId)
                .postName("post")
                .user(user)
                .description("description1")
                .voteCount(20)
                .subRead(subRead)
                .build());
        mockMvc.perform(get("/post/by-sub-read/" + subRead.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].postName", is("post")));
    }

    @Test
    @WithMockUser(username = username6)
    void get_PostsByUsername_Right() throws Exception {
        String subredditName = "getPostsByUsername1RightS";
        String password1 = "as";
        Long postId = 1L;
        Long subredditId = 1L;
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username6)
                .password(passwordEncoder.encode(password1))
                .enabled(true)
                .build());
        SubRead subRead = subReadRepository.save(SubRead.builder()
                .id(subredditId)
                .description("d1")
                .name(subredditName)
                .user(user)
                .build());
        postRepository.save(Post.builder()
                .createdDate(LocalDateTime.now())
                .id(postId)
                .postName("getPostsByUsername1RightP")
                .user(user)
                .description("getPostsByUsername1RightD")
                .voteCount(20)
                .subRead(subRead)
                .build());
        mockMvc.perform(get("/post/by-user/" + username6))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].postName", is("getPostsByUsername1RightP")))
                .andExpect(jsonPath("$.content[0].description", is("getPostsByUsername1RightD")));
    }
}
