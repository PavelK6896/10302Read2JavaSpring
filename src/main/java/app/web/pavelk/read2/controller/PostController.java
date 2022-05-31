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
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Operation(description = "Get post by id.")
    @Parameter(in = ParameterIn.PATH, name = "id", schema = @Schema(defaultValue = "1"))
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @Operation(description = "Get page post.")
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getPagePosts(@Parameter(hidden = true) Pageable pageable) {
        return postService.getPagePosts(pageable);
    }

    @Operation(description = "Get page post by sub read id.")
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Parameter(in = ParameterIn.PATH, name = "subReadId", schema = @Schema(defaultValue = "1"))
    @GetMapping("by-sub-read/{subReadId}")
    public ResponseEntity<Page<PostResponseDto>> getPagePostBySubReadId(@PathVariable Long subReadId, @Parameter(hidden = true) Pageable pageable) {
        return postService.getPagePostsBySubReadId(subReadId, pageable);
    }

    @Operation(description = "Get page post by user name.")
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Parameter(in = ParameterIn.PATH, name = "userName", schema = @Schema(defaultValue = "admin"))
    @GetMapping("by-user/{userName}")
    public ResponseEntity<Page<PostResponseDto>> getPagePostByUsername(@PathVariable String userName, @Parameter(hidden = true) Pageable pageable) {
        return postService.getPagePostsByUsername(userName, pageable);
    }

    @Operation(description = "Create post.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "post body", required = true,
                    content = @Content(schema = @Schema(implementation = PostRequestDto.class))))
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

}
