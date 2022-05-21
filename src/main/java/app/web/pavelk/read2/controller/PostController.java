package app.web.pavelk.read2.controller;


import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    @Qualifier("postServiceFirstImpl")
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @GetMapping
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(@Parameter(hidden = true) Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponseDto>> getPostsBySubreddit(@PathVariable Long id) {
        return postService.getPostsBySubreddit(id);
    }

    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUsername(@PathVariable String name) {
        return postService.getPostsByUsername(name);
    }
}
