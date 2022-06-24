package app.web.pavelk.read2.controller;

import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.schema.SubRead;
import app.web.pavelk.read2.schema.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.SUB_NOT_FOUND;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext
class SubReadControllerTest extends TestCommonController {

    @Test
    @WithMockUser(username = username1)
    void createNewSub_Right() throws Exception {
        String password1 = "dsd$%#@sdfs";
        User user = userRepository.save(User.builder()
                .created(Instant.now())
                .email("a@pvhfha.ru")
                .username(username1)
                .password(passwordEncoder.encode(password1))
                .enabled(true)
                .build());
        Long id = 1L;
        String name = "create sub-read";
        String description = "create sub-read";
        SubReadDto subReadDto = SubReadDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .numberOfPosts(1)
                .build();
        mockMvc.perform(post("/sub-read")
                        .content(objectMapper.writeValueAsString(subReadDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));
    }

    @Test
    void createNewSub_Wrong() throws Exception {
        Long id = 1L;
        String description = "create sub-read";
        SubReadDto subReadDto = SubReadDto.builder()
                .id(id)
                .name(null)
                .description(description)
                .numberOfPosts(1)
                .build();
        mockMvc.perform(post("/sub-read")
                        .content(objectMapper.writeValueAsString(subReadDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(content().string("Error server"));
    }


    @Test
    void get_AllSub_Right() throws Exception {
        String name1 = "get sub-read";
        String name2 = "get sub-read 2";
        String description = "get sub-read";
        SubRead subRead1 = subReadRepository.save(SubRead.builder()
                .name(name1)
                .createdDate(Instant.now())
                .description(description)
                .build());
        SubRead subRead2 = subReadRepository.save(SubRead.builder()
                .name(name2)
                .createdDate(Instant.now().minusSeconds(10))
                .description(description)
                .build());
        mockMvc.perform(get("/sub-read"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is(name1)))
                .andExpect(jsonPath("$.content[1].name", is(name2)))
                .andExpect(jsonPath("$.content[0].description", is(description)))
                .andExpect(jsonPath("$.content[1].description", is(description)))
                .andExpect(jsonPath("$.content[0].id", is(subRead1.getId().intValue())))
                .andExpect(jsonPath("$.content[1].id", is(subRead2.getId().intValue())));
    }

    @Test
    void get_AllSub_Wrong() throws Exception {
        mockMvc.perform(get("/sub-read"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    void get_SubById_Right() throws Exception {
        String name1 = "get sub-read";
        String description = "get sub-read";
        SubRead subRead1 = subReadRepository.save(SubRead.builder()
                .name(name1)
                .description(description)
                .build());
        mockMvc.perform(get("/sub-read/" + subRead1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name1)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.id", is(subRead1.getId().intValue())));
    }

    @Test
    void get_SubById_Wrong() throws Exception {
        long id = 2626L;
        mockMvc.perform(get("/sub-read/" + id))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(content().string(SUB_NOT_FOUND.getMessage().formatted(id)));
    }

}
