package app.web.pavelk.read2.service;

import app.web.pavelk.read2.dto.PostRequestDto;
import app.web.pavelk.read2.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Post logic.
 */
public interface PostService extends CommonService {

    /**
     * Create post.
     *
     * @param postRequestDto data post
     * @return status 201
     */
    ResponseEntity<Void> createPost(PostRequestDto postRequestDto);

    /**
     * Get post.
     *
     * @param postId id
     * @return post dto
     */
    ResponseEntity<PostResponseDto> getPost(Long postId);

    /**
     * Get page post.
     *
     * @param pageable page property
     * @return page posts dto
     */
    ResponseEntity<Page<PostResponseDto>> getPagePosts(Pageable pageable);

    /**
     * Get page post by sub read.
     *
     * @param subredditId id sub read
     * @param pageable    page property
     * @return page posts dto
     */
    ResponseEntity<Page<PostResponseDto>> getPagePostsBySubReadId(Long subredditId, Pageable pageable);

    /**
     * Get page post by username.
     *
     * @param username name
     * @param pageable page property
     * @return page posts dto
     */
    ResponseEntity<Page<PostResponseDto>> getPagePostsByUsername(String username, Pageable pageable);
}
