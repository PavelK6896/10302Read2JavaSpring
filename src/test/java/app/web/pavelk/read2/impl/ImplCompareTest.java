package app.web.pavelk.read2.impl;

import app.web.pavelk.read2.PostgresContainer;
import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.dto.SubReadDto;
import app.web.pavelk.read2.service.impl.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ImplCompareTest extends PostgresContainer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostServiceFirstImpl postServiceFirstImpl;
    @Autowired
    private PostServiceMapImpl postServiceMapImpl;
    @Autowired
    private PostServiceQueryImpl postServiceQueryImpl;
    @Autowired
    private CommentServiceFirstImpl commentServiceFirstImpl;
    @Autowired
    private CommentServiceQueryImpl commentServiceQueryImpl;
    @Autowired
    private SubReadServiceFirstImpl subReadServiceFirstImpl;
    @Autowired
    private SubReadServiceQueryImpl subReadServiceQueryImpl;


    @Test
    void getAllPosts() throws Exception {
        PageRequest of = PageRequest.of(0, 10, Sort.unsorted());
        ResponseEntity<Page<PostResponseDto>> response1 = postServiceFirstImpl.getPagePosts(of);
        ResponseEntity<Page<PostResponseDto>> response2 = postServiceMapImpl.getPagePosts(of);
        ResponseEntity<Page<PostResponseDto>> response3 = postServiceQueryImpl.getPagePosts(of);
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
        ResponseEntity<Void> response1 = postServiceFirstImpl.createPost(postRequestDto);
        ResponseEntity<Void> response2 = postServiceMapImpl.createPost(postRequestDto);
        ResponseEntity<Void> response3 = postServiceQueryImpl.createPost(postRequestDto);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

    @Test
    void getPost() throws Exception {
        ResponseEntity<PostResponseDto> response1 = postServiceFirstImpl.getPost(1L);
        ResponseEntity<PostResponseDto> response2 = postServiceMapImpl.getPost(1L);
        ResponseEntity<PostResponseDto> response3 = postServiceQueryImpl.getPost(1L);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

    @Test
    void getPostsBySubreddit() throws Exception {
        PageRequest of = PageRequest.of(0, 10, Sort.unsorted());
        ResponseEntity<Page<PostResponseDto>> response1 = postServiceFirstImpl.getPagePostsBySubReadId(1L, of);
        ResponseEntity<Page<PostResponseDto>> response2 = postServiceMapImpl.getPagePostsBySubReadId(1L, of);
        ResponseEntity<Page<PostResponseDto>> response3 = postServiceQueryImpl.getPagePostsBySubReadId(1L, of);
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
        ResponseEntity<Page<PostResponseDto>> response1 = postServiceFirstImpl.getPagePostsByUsername(NAME, of);
        ResponseEntity<Page<PostResponseDto>> response2 = postServiceFirstImpl.getPagePostsByUsername(NAME, of);
        ResponseEntity<Page<PostResponseDto>> response3 = postServiceQueryImpl.getPagePostsByUsername(NAME, of);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        String impl3 = objectMapper.writeValueAsString(response3);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(impl2, impl3, JSONCompareMode.STRICT);
    }

    @Test
    void getPageSubRead() throws Exception {
        PageRequest of = PageRequest.of(0, 10, Sort.unsorted());
        ResponseEntity<Page<SubReadDto>> response1 = subReadServiceFirstImpl.getPageSubRead(of);
        ResponseEntity<Page<SubReadDto>> response2 = subReadServiceQueryImpl.getPageSubRead(of);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
    }

    @Test
    void getSubReadById() throws Exception {
        ResponseEntity<SubReadDto> response1 = subReadServiceFirstImpl.getSubReadById(1L);
        ResponseEntity<SubReadDto> response2 = subReadServiceQueryImpl.getSubReadById(1L);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
    }

    @Test
    void getSliceCommentsForPost() throws Exception {
        PageRequest of = PageRequest.of(0, 10, Sort.unsorted());
        ResponseEntity<Slice<CommentsDto>> response1 = commentServiceFirstImpl.getSliceCommentsForPost(1L, of);
        ResponseEntity<Slice<CommentsDto>> response2 = commentServiceQueryImpl.getSliceCommentsForPost(1L, of);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
    }

    @Test
    void getSliceCommentsForUser() throws Exception {
        PageRequest of = PageRequest.of(0, 10, Sort.unsorted());
        ResponseEntity<Slice<CommentsDto>> response1 = commentServiceFirstImpl.getSliceCommentsForUser("admin", of);
        ResponseEntity<Slice<CommentsDto>> response2 = commentServiceQueryImpl.getSliceCommentsForUser("admin", of);
        String impl1 = objectMapper.writeValueAsString(response1);
        String impl2 = objectMapper.writeValueAsString(response2);
        JSONAssert.assertEquals(impl1, impl2, JSONCompareMode.STRICT);
    }

}
