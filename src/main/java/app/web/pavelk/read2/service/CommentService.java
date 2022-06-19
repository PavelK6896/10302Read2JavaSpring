package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.CommentsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;

/**
 * Service for working with comments
 */
public interface CommentService extends CommonService {
    /**
     * Create new comment.
     *
     * @param commentsDto need data
     * @return status 201
     */
    ResponseEntity<Void> createComment(CommentsDto commentsDto);

    /**
     * Get slice comment on post.
     *
     * @param postId   post id
     * @param pageable setting for get the part data
     * @return comments
     */
    ResponseEntity<Slice<CommentsDto>> getSliceCommentsForPost(Long postId, Pageable pageable);

    /**
     * Get slice comment on user.
     *
     * @param userName name user
     * @param pageable setting for get the part data
     * @return comments
     */
    ResponseEntity<Slice<CommentsDto>> getSliceCommentsForUser(String userName, Pageable pageable);
}
