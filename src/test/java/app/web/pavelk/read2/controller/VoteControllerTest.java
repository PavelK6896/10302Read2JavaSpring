package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.VoteDto;
import app.web.pavelk.read2.schema.Post;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.schema.VoteType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext
class VoteControllerTest extends TestCommonController {

    @Test
    @WithMockUser(username = username1)
    void vote_Right() throws Exception {
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
        mockMvc.perform(post("/vote")
                        .content(objectMapper.writeValueAsString(voteDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(202));

    }

    @Test
    @WithMockUser(username = username1)
    void vote_Wrong() throws Exception {
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
        mockMvc.perform(post("/vote")
                        .content(objectMapper.writeValueAsString(voteDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(500));
    }
}
