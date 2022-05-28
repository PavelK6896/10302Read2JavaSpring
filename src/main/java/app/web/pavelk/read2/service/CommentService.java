package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.CommentsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;

public interface CommentService extends CommonService {
    ResponseEntity<Void> createComment(CommentsDto commentsDto);

    ResponseEntity<Slice<CommentsDto>> getSliceCommentsForPost(Long postId, Pageable pageable);

    ResponseEntity<Slice<CommentsDto>> getSliceCommentsForUser(String userName, Pageable pageable);
}
