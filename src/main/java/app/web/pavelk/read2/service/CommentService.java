package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.CommentsDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    ResponseEntity<Void> createComment(CommentsDto commentsDto);

    ResponseEntity<List<CommentsDto>> getAllCommentsForPost(Long postId);

    ResponseEntity<List<CommentsDto>> getAllCommentsForUser(String userName);
}
