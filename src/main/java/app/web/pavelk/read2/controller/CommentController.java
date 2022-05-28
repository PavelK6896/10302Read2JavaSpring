package app.web.pavelk.read2.controller;


import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(description = "Get all comment by post id.")
    @Parameter(in = ParameterIn.PATH, name = "postId", schema = @Schema(defaultValue = "1"))
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Parameter(in = ParameterIn.QUERY, name = "sort", array = @ArraySchema(schema = @Schema(type = "string")))
    @GetMapping("/by-post/{postId}")
    public ResponseEntity<Slice<CommentsDto>> getSliceCommentsForPost(@PathVariable Long postId, @Parameter(hidden = true) Pageable pageable) {
        return commentService.getSliceCommentsForPost(postId, pageable);
    }

    @Operation(description = "Get all comment by user name.")
    @Parameter(in = ParameterIn.PATH, name = "userName", schema = @Schema(defaultValue = "admin"))
    @Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(defaultValue = "0"))
    @Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(defaultValue = "20"))
    @Parameter(in = ParameterIn.QUERY, name = "sort", array = @ArraySchema(schema = @Schema(type = "string")))
    @GetMapping("/by-user/{userName}")
    public ResponseEntity<Slice<CommentsDto>> getSliceCommentsForUser(@PathVariable String userName, @Parameter(hidden = true) Pageable pageable) {
        return commentService.getSliceCommentsForUser(userName, pageable);
    }

    @Operation(description = "Create comment.")
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        return commentService.createComment(commentsDto);
    }

}
