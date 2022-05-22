package app.web.pavelk.read2.controller;


import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import app.web.pavelk.read2.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(description = "create post",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "post body", required = true,
                    content = @Content(schema = @Schema(implementation = PostRequestDto.class))))
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @GetMapping
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Operation(description = "get all posts")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(@Parameter(hidden = true) Pageable pageable) {
        return postService.getPagePosts(pageable);
    }

    @GetMapping("/{id}")
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(defaultValue = "1"))
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("by-subreddit/{id}")
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(defaultValue = "1"))
    public ResponseEntity<Page<PostResponseDto>> getPostsBySubreddit(@PathVariable Long id, @Parameter(hidden = true) Pageable pageable) {
        return postService.getPagePostsBySubreddit(id, pageable);
    }

    @GetMapping("by-user/{name}")
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Parameter(in = ParameterIn.PATH, name = "name", schema = @Schema(defaultValue = "admin"))
    public ResponseEntity<Page<PostResponseDto>> getPostsByUsername(@PathVariable String name, @Parameter(hidden = true) Pageable pageable) {
        return postService.getPagePostsByUsername(name, pageable);
    }
}
