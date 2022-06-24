package app.web.pavelk.read2.controller;


import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.schema.Comment;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.schema.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.POST_NOT_FOUND;
import static app.web.pavelk.read2.exceptions.ExceptionMessage.USER_NOT_FOUND;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DirtiesContext
class CommentControllerTest extends TestCommonController {


    @Test
    @WithMockUser(username = username1)
    void createComment_Right() throws Exception {
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build());
        SubRead subRead = subReadRepository.save(SubRead.builder()
                .description("d1")
                .name("name1")
                .user(user)
                .build());
        Post post = postRepository.save(Post.builder()
                .id(1L)
                .postName("sz")
                .user(user)
                .description("11")
                .voteCount(0)
                .subRead(subRead)
                .build());
        CommentsDto commentsDto = CommentsDto.builder()
                .createdDate(Instant.now())
                .postId(post.getId())
                .text("comment1")
                .userName(username1)
                .build();

        mockMvc.perform(post("/comment/")
                        .content(objectMapper.writeValueAsString(commentsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));

    }

    @Test
    void createComment_PostNotFoundException_Wrong() throws Exception {
        CommentsDto commentsDto = CommentsDto.builder()
                .createdDate(Instant.now())
                .postId(2L)
                .text("comment1")
                .userName(username2)
                .build();

        mockMvc.perform(post("/comment/")
                        .content(objectMapper.writeValueAsString(commentsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(content().string(POST_NOT_FOUND.getMessage().formatted(commentsDto.getPostId())));
    }

    @Test
    @WithMockUser(username = username2)
    void createComment_UsernameNotFoundException_Wrong() throws Exception {
        SubRead subRead = subReadRepository.save(SubRead.builder()
                .description("d1")
                .name("name1")
                .user(null)
                .build());
        Post post = postRepository.save(Post.builder()
                .id(1L)
                .postName("sz")
                .user(null)
                .description("11")
                .voteCount(0)
                .subRead(subRead)
                .build());
        CommentsDto comment1 = CommentsDto.builder()
                .createdDate(Instant.now())
                .postId(post.getId())
                .text("comment1")
                .userName(username2)
                .build();
        mockMvc.perform(post("/comment/")
                        .content(objectMapper.writeValueAsString(comment1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(content().string(USER_NOT_FOUND.getMessage().formatted(username2)));
    }


    @Test
    void get_AllCommentsForPost_Right() throws Exception {
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build());
        Post post = postRepository.save(Post.builder()
                .id(12L)
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
        mockMvc.perform(get("/comment/by-post/" + post.getId()))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void get_AllCommentsForUser_Right() throws Exception {
        long postId = 14L;
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build());
        Post post = postRepository.save(Post.builder()
                .id(postId)
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
        mockMvc.perform(get("/comment/by-user/" + username1))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

}
