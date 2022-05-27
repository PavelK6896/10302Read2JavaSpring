package app.web.pavelk.read2.controller;


import app.web.pavelk.read2.dto.CommentsDto;
import app.web.pavelk.read2.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(description = "Get all comment by post id.")
    @Parameter(in = ParameterIn.PATH, name = "postId", schema = @Schema(defaultValue = "1"))
    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return commentService.getAllCommentsForPost(postId);
    }

    @Operation(description = "Get all comment by user name.")
    @Parameter(in = ParameterIn.PATH, name = "userName", schema = @Schema(defaultValue = "admin"))
    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName) {
        return commentService.getAllCommentsForUser(userName);
    }

    @Operation(description = "Create comment.")
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        return commentService.createComment(commentsDto);
    }

}
